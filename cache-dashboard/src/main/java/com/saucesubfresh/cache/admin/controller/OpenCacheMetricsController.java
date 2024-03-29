package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheMetricsService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 返回某个应用的某个 cacheName 全部节点指标数据
     */
    @GetMapping("/page")
    public Result<PageResult<OpenCacheMetricsRespDTO>> page(OpenCacheMetricsReqDTO reqDTO) {
        return Result.succeed(openCacheMetricsService.selectPage(reqDTO));
    }

}
