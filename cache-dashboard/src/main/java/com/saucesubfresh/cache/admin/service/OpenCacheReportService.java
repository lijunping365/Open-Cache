package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenTopKRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenCacheReportService {

    void generateReport(LocalDateTime now);

    OpenCacheStatisticRespDTO getStatistic();

    OpenCacheStatisticRespDTO getAppStatistic(Long appId);

    OpenCacheStatisticRespDTO getCacheNameStatistic(Long appId, String cacheName);

    OpenCacheStatisticRespDTO getInstanceStatistic(Long appId, String serverId);

    List<OpenCacheChartRespDTO> getChart(Long appId, String cacheName, String instanceId, Integer count);

    List<OpenTopKRespDTO> getCacheNameTopK(Long appId, String instanceId, Integer count, Integer top);

    List<OpenTopKRespDTO> getInstanceTopK(Long appId, String cacheName, Integer count, Integer top);
}
