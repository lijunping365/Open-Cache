package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;
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
    public Result<OpenCacheStatisticRespDTO> getStatistic(@RequestParam("appId") Long appId) {
        return Result.succeed(cacheReportService.getStatistic(appId));
    }

    @GetMapping("/chart")
    public Result<List<OpenCacheChartRespDTO>> getChart(@RequestParam(value = "appId") Long appId,
                                                        @RequestParam(value = "count", required = false, defaultValue = "30") Integer count) {
        return Result.succeed(cacheReportService.getChart(appId, count));
    }
}
