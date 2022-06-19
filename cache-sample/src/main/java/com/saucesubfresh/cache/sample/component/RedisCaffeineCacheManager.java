package com.saucesubfresh.cache.sample.component;

import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.core.ClusterCacheProvider;
import com.saucesubfresh.starter.cache.manager.AbstractCacheManager;
import com.saucesubfresh.starter.cache.properties.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author: 李俊平
 * @Date: 2022-06-18 23:13
 */
@Component
public class RedisCaffeineCacheManager extends AbstractCacheManager {

    private static final String SAM = ":";
    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisCaffeineCacheManager(CacheProperties properties, RedisTemplate<Object, Object> redisTemplate) {
        super(properties);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected ClusterCache createCache(String cacheName, CacheConfig cacheConfig) {
        String namespace = properties.getNamespace();
        cacheName = generate(namespace, cacheName);
        return new RedisCaffeineCache(cacheName, redisTemplate);
    }

    protected String generate(String namespace, String cacheName){
        return namespace + SAM + cacheName;
    }
}
