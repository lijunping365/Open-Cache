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

    OpenCacheStatisticRespDTO getStatistic(Long appId);

    List<OpenCacheChartRespDTO> getChart(Long appId, String cacheName, String instanceId, Integer count);


    default List<OpenTopKRespDTO> getCacheNameTopK(Long appId, String instanceId, Integer count, Integer top){
        return getTopK(appId, null, instanceId, "cacheName", count, top);
    }

    default List<OpenTopKRespDTO> getInstanceTopK(Long appId, String cacheName, Integer count, Integer top){
        return getTopK(appId, cacheName, null, "instanceId", count, top);
    }

    List<OpenTopKRespDTO> getTopK(Long appId, String cacheName, String instanceId, String type, Integer count, Integer top);
}
