package com.saucesubfresh.cache.sample.component;

import com.saucesubfresh.starter.cache.core.AbstractClusterCache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: 李俊平
 * @Date: 2022-06-18 23:33
 */
public class RedisCaffeineCache extends AbstractClusterCache {

    private final String cacheName;
    private final RedisTemplate<Object, Object> redisTemplate;


    public RedisCaffeineCache(String cacheName, RedisTemplate<Object, Object> redisTemplate) {
        this.cacheName = cacheName;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public void put(Object o, Object o1) {

    }

    @Override
    public void evict(Object o) {

    }

    @Override
    public void clear() {

    }
}
