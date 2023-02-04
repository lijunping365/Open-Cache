package com.saucesubfresh.cache.sample.processor;

import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageResponse;
import com.saucesubfresh.cache.common.domain.CacheNameInfo;
import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.json.JSON;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.server.process.MessageProcess;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess {

    private final CacheManager cacheManager;

    public CacheMessageProcessor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        CacheMessageRequest request = SerializationUtils.deserialize(body, CacheMessageRequest.class);
        CacheCommandEnum command = CacheCommandEnum.of(request.getCommand());
        if (Objects.isNull(command)){
            throw new RpcException("the parameter command must not be null");
        }

        String cacheName = request.getCacheName();
        Object key = request.getKey();
        Object value = request.getValue();
        ClusterCache cache = cacheManager.getCache(cacheName);
        CacheMessageResponse response = new CacheMessageResponse();
        try {
            switch (command){
                case CLEAR:
                    cache.clear();
                    break;
                case INVALIDATE:
                    cache.evict(key);
                    break;
                case PRELOAD:
                    cache.preloadCache();
                    break;
                case UPDATE:
                    cache.put(key, value);
                    break;
                case GET:
                    Object o = cache.get(key);
                    response.setData(Objects.isNull(o) ? null : JSON.toJSON(o));
                    break;
                case QUERY_CACHE_NAMES:
                    List<CacheNameInfo> cacheNames = getCacheName();
                    response.setData(JSON.toJSON(cacheNames));
                    break;
                case QUERY_CACHE_METRICS:
                    List<CacheStatsInfo> cacheMetrics = getCacheMetrics(request);
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

    private List<CacheNameInfo> getCacheName(){
        return cacheManager.getCacheNames().stream().map(e->{
            CacheNameInfo cacheNameInfo = new CacheNameInfo();
            cacheNameInfo.setCacheName(e);
            final ClusterCache cache = cacheManager.getCache(e);
            cacheNameInfo.setLocalCacheKeySize(cache.getLocalCacheCount());
            cacheNameInfo.setRemoteCacheKeySize(cache.getRemoteCacheCount());
            return cacheNameInfo;
        }).collect(Collectors.toList());
    }

    private List<CacheStatsInfo> getCacheMetrics(CacheMessageRequest messageBody){
        List<String> cacheNames = new ArrayList<>();
        String cacheName = messageBody.getCacheName();
        if (StringUtils.isNotBlank(cacheName)){
            cacheNames.add(cacheName);
        }else {
            cacheNames.addAll(cacheManager.getCacheNames());
        }

        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }

        return cacheNames.stream().map(e->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(e);
            final ClusterCache cache = cacheManager.getCache(cacheName);
            final CacheStats stats = cache.getStats();
            cacheStatsInfo.setHitCount(stats.getHitCount());
            cacheStatsInfo.setMissCount(stats.getMissCount());
            cacheStatsInfo.setRequestCount(stats.requestCount());
            cacheStatsInfo.setHitRate(stats.hitRate());
            cacheStatsInfo.setMissRate(stats.missRate());
            return cacheStatsInfo;
        }).collect(Collectors.toList());
    }
}
