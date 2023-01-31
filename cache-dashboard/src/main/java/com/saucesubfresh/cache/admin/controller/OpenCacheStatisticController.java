package com.saucesubfresh.cache.admin.controller;


import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticNumberRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticReportRespDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheStatisticService;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Validated
@RestController
@RequestMapping("/statistic")
public class OpenCacheStatisticController {

    @Autowired
    private OpenCacheStatisticService crawlerStatisticService;

    @GetMapping("/number")
    public Result<OpenCacheStatisticNumberRespDTO> getStatisticNumber() {
        return Result.succeed(crawlerStatisticService.getStatisticNumber());
    }

    @GetMapping("/report")
    public Result<List<OpenCacheStatisticReportRespDTO>> getStatisticReport() {
        return Result.succeed(crawlerStatisticService.getStatisticReport());
    }
}
