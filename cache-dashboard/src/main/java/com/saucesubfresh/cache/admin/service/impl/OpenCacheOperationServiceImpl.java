package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheOperationService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheClearCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheEvictCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePreloadCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUpdateCacheRequest;
import com.saucesubfresh.rpc.client.remoting.RemotingInvoker;
import org.springframework.stereotype.Service;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheOperationServiceImpl implements OpenCacheOperationService {

    private final RemotingInvoker remotingInvoker;

    public OpenCacheOperationServiceImpl(RemotingInvoker remotingInvoker) {
        this.remotingInvoker = remotingInvoker;
    }

    @Override
    public boolean preloadCache(OpenCachePreloadCacheRequest request) {
        remotingInvoker.invoke(null, null);
        return true;
    }

    @Override
    public boolean clearCache(OpenCacheClearCacheRequest request) {
        remotingInvoker.invoke(null, null);
        return true;
    }

    @Override
    public boolean evictCache(OpenCacheEvictCacheRequest request) {
        remotingInvoker.invoke(null, null);
        return true;
    }

    @Override
    public boolean updateCache(OpenCacheUpdateCacheRequest request) {
        remotingInvoker.invoke(null, null);
        return true;
    }
}
