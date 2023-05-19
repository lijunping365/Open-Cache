package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenCacheReportService {

    void generateReport(LocalDateTime now);

    OpenCacheStatisticRespDTO getStatistic(Long appId);

    List<OpenCacheChartRespDTO> getAppChart(Long appId, Integer count);

    List<OpenCacheChartRespDTO> getInstanceChart(Long appId, String instanceId, Integer count);

    List<OpenCacheChartRespDTO> getCacheNameChart(Long appId, String cacheName, Integer count);
}
