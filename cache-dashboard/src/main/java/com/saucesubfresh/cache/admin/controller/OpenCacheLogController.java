package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheLogRespDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheLogService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Validated
@RestController
@RequestMapping("/logger")
public class OpenCacheLogController {

  @Autowired
  private OpenCacheLogService openCacheLogService;

  @GetMapping("/page")
  public Result<PageResult<OpenCacheLogRespDTO>> page(OpenCacheLogReqDTO openCacheLogReqDTO) {
    return Result.succeed(openCacheLogService.selectPage(openCacheLogReqDTO));
  }

  @GetMapping("/info/{id}")
  public Result<OpenCacheLogRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(openCacheLogService.getById(id));
  }

  @PutMapping("/delete/{id}")
  public Result<Boolean> delete(@PathVariable("id") Long id) {
    return Result.succeed(openCacheLogService.deleteById(id));
  }

}
