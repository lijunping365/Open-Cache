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
     * 获取当前指标数据
     * @param reqDTO
     * @return
     */
    @GetMapping("/current")
    public Result<List<OpenCacheMetricsRespDTO>> current(OpenCacheMetricsReqDTO reqDTO) {
        return Result.succeed(openCacheMetricsService.select(reqDTO));
    }

    /**
     * 获取历史指标数据
     * @param reqDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<OpenCacheLogRespDTO>> page(OpenCacheMetricsReqDTO reqDTO) {
        return Result.succeed(openCacheMetricsService.selectPage(reqDTO));
    }

    /**
     * 获取某一时刻应用内个节点缓存指标对比图数据
     * @param time
     * @return
     */
    @GetMapping("/chart")
    public Result<List<OpenCacheLogRespDTO>> info(@RequestParam("time") String time) {
        return Result.succeed(openCacheMetricsService.getById(id));
    }

    @PutMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        return Result.succeed(openCacheMetricsService.deleteById(id));
    }

}
