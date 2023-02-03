package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
public interface OpenCacheMetricsService {

    PageResult<OpenCacheMetricsRespDTO> queryMetrics(OpenCacheMetricsReqDTO reqDTO);
}
