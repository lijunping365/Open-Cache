package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenCacheReportService {

    OpenCacheStatisticRespDTO getStatistic(Long appId);

    List<OpenCacheChartRespDTO> getChart(Long appId, Integer count);
}
