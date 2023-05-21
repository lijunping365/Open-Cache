package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenTopKRespDTO;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Validated
@RestController
@RequestMapping("/analysis")
public class OpenCacheReportController {

    @Autowired
    private OpenCacheReportService cacheReportService;

    @GetMapping("/statistic")
    public Result<OpenCacheStatisticRespDTO> getStatistic() {
        return Result.succeed(cacheReportService.getStatistic());
    }

    @GetMapping("/appStatistic")
    public Result<OpenCacheStatisticRespDTO> getAppStatistic(@RequestParam("appId") Long appId) {
        return Result.succeed(cacheReportService.getAppStatistic(appId));
    }

    @GetMapping("/cacheNameStatistic")
    public Result<OpenCacheStatisticRespDTO> getJobStatistic(@RequestParam("appId") Long appId,
                                                             @RequestParam("cacheName") String cacheName) {
        return Result.succeed(cacheReportService.getCacheNameStatistic(appId, cacheName));
    }

    @GetMapping("/instanceStatistic")
    public Result<OpenCacheStatisticRespDTO> getInstanceStatistic(@RequestParam("appId") Long appId,
                                                                  @RequestParam("serverId") String serverId) {
        return Result.succeed(cacheReportService.getInstanceStatistic(appId, serverId));
    }

    @GetMapping("/chart")
    public Result<List<OpenCacheChartRespDTO>> getChart(@RequestParam(value = "appId") Long appId,
                                                        @RequestParam(value = "cacheName", required = false) String cacheName,
                                                        @RequestParam(value = "instanceId", required = false) String instanceId,
                                                        @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return Result.succeed(cacheReportService.getChart(appId, cacheName, instanceId, count));
    }

    @GetMapping("/cacheNameTok")
    public Result<List<OpenTopKRespDTO>> getCacheNameTopK(@RequestParam(value = "appId") Long appId,
                                                          @RequestParam(value = "instanceId", required = false) String instanceId,
                                                          @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                          @RequestParam(value = "top", required = false, defaultValue = "10") Integer top) {
        return Result.succeed(cacheReportService.getCacheNameTopK(appId, instanceId, count, top));
    }

    @GetMapping("/instanceTok")
    public Result<List<OpenTopKRespDTO>> getInstanceTopK(@RequestParam(value = "appId") Long appId,
                                                         @RequestParam(value = "cacheName", required = false) String cacheName,
                                                         @RequestParam(value = "count", required = false, defaultValue = "30") Integer count,
                                                         @RequestParam(value = "top", required = false, defaultValue = "10") Integer top) {
        return Result.succeed(cacheReportService.getInstanceTopK(appId, cacheName, count, top));
    }
}
