package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheMetricsService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.rpc.client.remoting.RemotingInvoker;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheMetricsServiceImpl implements OpenCacheMetricsService {

    private final RemotingInvoker remotingInvoker;

    public OpenCacheMetricsServiceImpl(RemotingInvoker remotingInvoker) {
        this.remotingInvoker = remotingInvoker;
    }


    @Override
    public List<OpenCacheMetricsRespDTO> select(OpenCacheMetricsReqDTO reqDTO) {
        remotingInvoker.invoke(null, null);
        return null;
    }
}
