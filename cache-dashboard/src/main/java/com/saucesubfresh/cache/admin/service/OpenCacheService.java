package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.req.*;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheNameRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheValueRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

/**
 * @author lijunping on 2023/1/31
 */
public interface OpenCacheService {

    /**
     * 查询 cacheNames
     */
    PageResult<OpenCacheNameRespDTO> cacheNames(OpenCacheNameReqDTO reqDTO);

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
     * @param request
     */
    boolean evictCache(OpenCacheEvictCacheRequest request);

    /**
     * 添加缓存
     * @param request
     */
    boolean updateCache(OpenCacheUpdateCacheRequest request);

    /**
     * 根据 key 清除
     * @param request
     */
    OpenCacheValueRespDTO getCache(OpenCacheGetCacheRequest request);
}
