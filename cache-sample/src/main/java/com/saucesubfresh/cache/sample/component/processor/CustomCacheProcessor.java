package com.saucesubfresh.cache.sample.component.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.message.CacheMessageProducer;
import com.saucesubfresh.starter.cache.processor.AbstractCacheProcessor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author lijunping
 */
@Slf4j
@Component
public class CustomCacheProcessor extends AbstractCacheProcessor {

    private static final ObjectMapper mapper = new TypedJsonJacksonCodec(Object.class, Object.class).getObjectMapper();

    private final CacheMessageProducer messageProducer;

    public CustomCacheProcessor(CacheManager cacheManager, CacheMessageProducer messageProducer) {
        super(cacheManager);
        this.messageProducer = messageProducer;
    }

    @Override
    public Object handlerCacheable(Supplier<Object> callback, String cacheName, String cacheKey, Class<?> returnType) throws Throwable {
        Object value;
        final ClusterCache cache = super.getCache(cacheName);
        value = cache.get(cacheKey);
        if (Objects.nonNull(value)){
            return process(value, returnType);
        }
        value = callback.get();
        cache.put(cacheKey, value);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.UPDATE)
                .key(cacheKey)
                .value(value)
                .build();
        publish(cacheMessage);
        return value;
    }

    @Override
    public void handlerCacheEvict(String cacheName, String cacheKey) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.evict(cacheKey);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.INVALIDATE)
                .key(cacheKey)
                .build();
        publish(cacheMessage);
    }

    @Override
    public void handlerCacheClear(String cacheName) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.clear();

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.CLEAR)
                .build();
        publish(cacheMessage);
    }

    @Override
    public void handlerCachePut(String cacheName, String cacheKey, Object cacheValue) throws Throwable {
        final ClusterCache cache = super.getCache(cacheName);
        cache.put(cacheKey, cacheValue);

        CacheMessage cacheMessage = CacheMessage.builder()
                .cacheName(cacheName)
                .command(CacheCommand.UPDATE)
                .key(cacheKey)
                .value(cacheValue)
                .build();
        publish(cacheMessage);
    }

    /**
     * <p>
     *     发布同步缓存广播消息
     * </p>
     *
     * @param message 同步缓存消息
     */
    protected void publish(CacheMessage message){
        messageProducer.broadcastLocalCacheStore(message);
    }

    /**
     * <p>
     *     发布同步缓存广播消息
     * </p>
     *
     * @param value 缓存值
     * @param returnType 方法返回类型
     */
    private Object process(Object value, Class<?> returnType) throws JsonProcessingException {
        Object result = fromStoreValue(value);
        if (null == result || returnType == Void.TYPE){
            return result;
        }
        return mapper.readValue(mapper.writeValueAsString(result), returnType);
    }

}
