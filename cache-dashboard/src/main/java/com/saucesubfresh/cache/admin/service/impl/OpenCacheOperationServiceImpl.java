package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheOperationService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheClearCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheEvictCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePreloadCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUpdateCacheRequest;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import org.springframework.stereotype.Service;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheOperationServiceImpl implements OpenCacheOperationService {

    private final ClusterInvoker clusterInvoker;

    public OpenCacheOperationServiceImpl(ClusterInvoker clusterInvoker) {
        this.clusterInvoker = clusterInvoker;
    }

    @Override
    public boolean preloadCache(OpenCachePreloadCacheRequest request) {
        clusterInvoker.invoke(null);
        return true;
    }

    @Override
    public boolean clearCache(OpenCacheClearCacheRequest request) {
        clusterInvoker.invoke(null);
        return true;
    }

    @Override
    public boolean evictCache(OpenCacheEvictCacheRequest request) {
        clusterInvoker.invoke(null);
        return true;
    }

    @Override
    public boolean updateCache(OpenCacheUpdateCacheRequest request) {
        clusterInvoker.invoke(null);
        return true;
    }
}
