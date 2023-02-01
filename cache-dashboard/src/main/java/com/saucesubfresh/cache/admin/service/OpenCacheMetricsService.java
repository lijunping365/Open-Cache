package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;

import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
public interface OpenCacheMetricsService {

    List<OpenCacheMetricsRespDTO> selectCurrent(OpenCacheMetricsReqDTO reqDTO);
}
