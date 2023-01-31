package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.req.OpenCacheClearCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheEvictCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePreloadCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUpdateCacheRequest;

/**
 * @author lijunping on 2023/1/31
 */
public interface OpenCacheOperationService {

    /**
     * 二级缓存数据加载到一级缓存
     */
    boolean preloadCache(OpenCachePreloadCacheRequest request);

    /**
     * 清空该 cacheName 下的缓存
     */
    boolean clearCache(OpenCacheClearCacheRequest request);

    /**
     * 根据 key 清除
     * @param key
     */
    boolean evictCache(OpenCacheEvictCacheRequest request);

    /**
     * 添加缓存
     * @param key
     * @param value
     */
    boolean updateCache(OpenCacheUpdateCacheRequest request);
}
