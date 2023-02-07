package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheService;
import com.saucesubfresh.cache.api.dto.req.*;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheKeyRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheNameRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheValueRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lijunping on 2023/1/31
 */
@Validated
@RestController
@RequestMapping("/cache")
public class OpenCacheController {

    @Autowired
    private OpenCacheService openCacheService;

    @GetMapping("/cacheNames")
    public Result<PageResult<OpenCacheNameRespDTO>> cacheNames(OpenCacheNameReqDTO reqDTO) {
        return Result.succeed(openCacheService.cacheNames(reqDTO));
    }

    @GetMapping("/cacheKeys")
    public Result<PageResult<OpenCacheKeyRespDTO>> cacheKeys(OpenCacheKeyReqDTO reqDTO) {
        return Result.succeed(openCacheService.cacheKeys(reqDTO));
    }

    @PostMapping("/get")
    public Result<OpenCacheValueRespDTO> getCache(@RequestBody @Valid OpenCacheGetCacheRequest request) {
        return Result.succeed(openCacheService.getCache(request));
    }

    @PostMapping("/preload")
    public Result<Boolean> preloadCache(@RequestBody @Valid OpenCachePreloadCacheRequest request) {
        return Result.succeed(openCacheService.preloadCache(request));
    }

    @PostMapping("/clear")
    public Result<Boolean> clearCache(@RequestBody @Valid OpenCacheClearCacheRequest request) {
        return Result.succeed(openCacheService.clearCache(request));
    }

    @PostMapping("/evict")
    public Result<Boolean> evictCache(@RequestBody @Valid OpenCacheEvictCacheRequest request) {
        return Result.succeed(openCacheService.evictCache(request));
    }

    @PostMapping("/update")
    public Result<Boolean> updateCache(@RequestBody @Valid OpenCacheUpdateCacheRequest request) {
        return Result.succeed(openCacheService.updateCache(request));
    }
}
