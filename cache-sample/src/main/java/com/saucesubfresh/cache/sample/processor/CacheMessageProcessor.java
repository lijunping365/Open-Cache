package com.saucesubfresh.cache.sample.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucesubfresh.cache.common.domain.*;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.json.JSON;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.server.process.MessageProcess;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess {

    private final CacheManager cacheManager;
    private final CacheProperties cacheProperties;

    private static final ObjectMapper mapper = new JsonJacksonCodec().getObjectMapper();

    public CacheMessageProcessor(CacheManager cacheManager, CacheProperties cacheProperties) {
        this.cacheManager = cacheManager;
        this.cacheProperties = cacheProperties;
    }

    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        CacheMessageRequest request = SerializationUtils.deserialize(body, CacheMessageRequest.class);
        CacheCommandEnum command = CacheCommandEnum.of(request.getCommand());
        if (Objects.isNull(command)){
            throw new RpcException("the parameter command must not be null");
        }

        List<String> keys = request.getKeys();
        List<String> cacheNames = request.getCacheNames();
        CacheMessageResponse response = new CacheMessageResponse();
        try {
            switch (command){
                case CLEAR:
                    cacheNames.forEach(e-> cacheManager.getCache(e).clear());
                    break;
                case INVALIDATE:
                    keys.forEach(e-> cacheManager.getCache(cacheNames.get(0)).evict(e));
                    break;
                case PRELOAD:
                    cacheNames.forEach(e->cacheManager.getCache(e).preloadCache());
                    break;
                case UPDATE:
                    Object value = mapper.readValue(request.getValue(), Object.class);
                    cacheManager.getCache(cacheNames.get(0)).put(keys.get(0), value);
                    break;
                case GET:
                    Object o = cacheManager.getCache(cacheNames.get(0)).get(keys.get(0));
                    response.setData(Objects.isNull(o) ? null : mapper.writeValueAsString(o));
                    break;
                case QUERY_CACHE_NAMES:
                    CacheNamePageInfo cacheNamePageInfo = getCacheName(request);
                    response.setData(JSON.toJSON(cacheNamePageInfo));
                    break;
                case QUERY_CACHE_KEY_SET:
                    CacheKeyPageInfo cacheKeyPageInfo = getCacheKeys(request);
                    response.setData(JSON.toJSON(cacheKeyPageInfo));
                    break;
                case QUERY_CACHE_METRICS:
                    CacheStatsInfo cacheMetrics = getCacheMetrics(cacheNames.get(0));
                    response.setData(JSON.toJSON(cacheMetrics));
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported Operation");
            }
        }catch (Exception e){
            throw new RpcException(e.getMessage());
        }
        return SerializationUtils.serialize(response);
    }

    private CacheKeyPageInfo getCacheKeys(CacheMessageRequest request){
        CacheKeyPageInfo cacheKeyPageInfo = new CacheKeyPageInfo();
        String cacheName = request.getCacheNames().get(0);
        String keyPattern = request.getCacheKeyPattern();
        Integer keyCount = request.getCacheKeyCount();
        Set<Object> cacheKeySet = cacheManager.getCache(cacheName).getCacheKeySet(keyPattern, keyCount);
        int totalSize = cacheKeySet.size();
        cacheKeyPageInfo.setTotalSize(totalSize);

        if (CollectionUtils.isEmpty(cacheKeySet)){
            return cacheKeyPageInfo;
        }

        Integer pageNum = request.getCurrent();
        Integer pageSize = request.getPageSize();
        Object[] objects = cacheKeySet.toArray();

        //进行分页处理
        if ((pageNum -1) * pageSize >= totalSize){
            return cacheKeyPageInfo;
        }
        int endIndex = Math.min(totalSize, pageNum * pageSize);

        List<String> cacheKeys = new ArrayList<>();
        for (int i = (pageNum -1) * pageSize; i < endIndex; i++) {
            cacheKeys.add((String) objects[i]);
        }
        cacheKeyPageInfo.setCacheKeys(cacheKeys);
        return cacheKeyPageInfo;
    }

    private CacheNamePageInfo getCacheName(CacheMessageRequest request){
        CacheNamePageInfo pageInfo = new CacheNamePageInfo();
        Collection<String> cacheNames = cacheManager.getCacheNames();

        int totalSize = cacheNames.size();
        pageInfo.setTotalSize(totalSize);

        if (CollectionUtils.isEmpty(cacheNames)){
            return pageInfo;
        }

        Integer pageNum = request.getCurrent();
        Integer pageSize = request.getPageSize();
        List<String> list = new ArrayList<>(cacheNames);
        //进行分页处理
        if ((pageNum -1) * pageSize >= totalSize){
            return pageInfo;
        }
        int endIndex = Math.min(totalSize, pageNum * pageSize);

        List<CacheNameInfo> cacheNameInfos = new ArrayList<>();
        for (int i = (pageNum -1) * pageSize; i < endIndex; i++) {
            String cacheName = list.get(i);
            CacheNameInfo cacheNameInfo = new CacheNameInfo();
            cacheNameInfo.setCacheName(cacheName);
            final ClusterCache cache = cacheManager.getCache(cacheName);
            cacheNameInfo.setCacheKeySize(cache.getCacheKeyCount());
            cacheNameInfos.add(cacheNameInfo);
        }
        pageInfo.setCacheNames(cacheNameInfos);
        return pageInfo;

    }

    private CacheStatsInfo getCacheMetrics(String cacheName){
        CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
        final ClusterCache cache = cacheManager.getCache(cacheName);
        final CacheStats stats = cache.getStats();
        cacheStatsInfo.setInstanceId(cacheProperties.getInstanceId());
        cacheStatsInfo.setHitCount(stats.getHitCount());
        cacheStatsInfo.setMissCount(stats.getMissCount());
        cacheStatsInfo.setRequestCount(stats.requestCount());
        cacheStatsInfo.setHitRate(stats.hitRate());
        cacheStatsInfo.setMissRate(stats.missRate());
        return cacheStatsInfo;
    }
}
