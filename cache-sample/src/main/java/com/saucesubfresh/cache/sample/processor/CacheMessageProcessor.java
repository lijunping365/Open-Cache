package com.saucesubfresh.cache.sample.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    private static final ObjectMapper mapper = new JsonJacksonCodec().getObjectMapper();

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

        String key = request.getKey();
        CacheMessageResponse response = new CacheMessageResponse();
        try {
            switch (command){
                case CLEAR:
                    request.getCacheNames().forEach(e-> cacheManager.getCache(e).clear());
                    break;
                case INVALIDATE:
                    cacheManager.getCache(request.getCacheNames().get(0)).evict(key);
                    break;
                case PRELOAD:
                    request.getCacheNames().forEach(e->cacheManager.getCache(e).preloadCache());
                    break;
                case UPDATE:
                    Object value = mapper.readValue(request.getValue(), Object.class);
                    cacheManager.getCache(request.getCacheNames().get(0)).put(key, value);
                    break;
                case GET:
                    Object o = cacheManager.getCache(request.getCacheNames().get(0)).get(key);
                    response.setData(Objects.isNull(o) ? null : mapper.writeValueAsString(o));
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
            cacheNameInfo.setCacheKeySize(cache.getCacheKeyCount());
            return cacheNameInfo;
        }).collect(Collectors.toList());
    }

    private List<CacheStatsInfo> getCacheMetrics(CacheMessageRequest messageBody){
        List<String> cacheNames = messageBody.getCacheNames();

        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }

        return cacheNames.stream().map(e->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(e);
            final ClusterCache cache = cacheManager.getCache(e);
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
