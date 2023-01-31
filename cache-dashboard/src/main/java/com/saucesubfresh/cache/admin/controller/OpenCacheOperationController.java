package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheOperationService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheClearCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheEvictCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePreloadCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUpdateCacheRequest;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lijunping on 2023/1/31
 */
@Validated
@RestController
@RequestMapping("/operation")
public class OpenCacheOperationController {

    @Autowired
    private OpenCacheOperationService openCacheOperationService;

    @PostMapping("/preload")
    public Result<Boolean> preloadCache(@RequestBody @Valid OpenCachePreloadCacheRequest request) {
        return Result.succeed(openCacheOperationService.preloadCache(request));
    }

    @PostMapping("/clear")
    public Result<Boolean> clearCache(@RequestBody @Valid OpenCacheClearCacheRequest request) {
        return Result.succeed(openCacheOperationService.clearCache(request));
    }

    @PostMapping("/evict")
    public Result<Boolean> evictCache(@RequestBody @Valid OpenCacheEvictCacheRequest request) {
        return Result.succeed(openCacheOperationService.evictCache(request));
    }

    @PostMapping("/update")
    public Result<Boolean> updateCache(@RequestBody @Valid OpenCacheUpdateCacheRequest request) {
        return Result.succeed(openCacheOperationService.updateCache(request));
    }
}
