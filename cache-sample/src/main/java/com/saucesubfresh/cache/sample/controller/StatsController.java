package com.saucesubfresh.cache.sample.controller;

import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.cache.sample.service.CacheStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijunping on 2022/6/23
 */
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private CacheStatsService cacheStatsService;

    @GetMapping("/load")
    public Result<List<CacheStatsInfo>> loadStats(){
        List<CacheStatsInfo> cacheStatsInfos = cacheStatsService.getCacheStatsInfo();
        return Result.succeed(cacheStatsInfos);
    }
}
