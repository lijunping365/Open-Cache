package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheMetricsService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheLogRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
@Validated
@RestController
@RequestMapping("/metrics")
public class OpenCacheMetricsController {

    @Autowired
    private OpenCacheMetricsService openCacheMetricsService;

    /**
     * 返回当前某个应用的某个 cacheName 全部节点指标数据
     */
    @GetMapping("/current/allNode")
    public Result<PageResult<OpenCacheMetricsRespDTO>> queryMetrics(OpenCacheMetricsReqDTO reqDTO) {
        return Result.succeed(openCacheMetricsService.queryMetrics(reqDTO));
    }

}
