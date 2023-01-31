package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.api.dto.create.OpenCacheAppCreateDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheAppReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheAppUpdateDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheAppService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * app
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Validated
@RestController
@RequestMapping("/app")
public class OpenCacheAppController {

  @Autowired
  private OpenCacheAppService openCacheAppService;

  @GetMapping("/page")
  public Result<PageResult<OpenCacheAppRespDTO>> page(OpenCacheAppReqDTO openCacheAppReqDTO) {
    return Result.succeed(openCacheAppService.selectPage(openCacheAppReqDTO));
  }

  @GetMapping("/list")
  public Result<List<OpenCacheAppRespDTO>> list(@RequestParam(required = false) String appName) {
    return Result.succeed(openCacheAppService.queryList(appName));
  }

  @GetMapping("/info/{id}")
  public Result<OpenCacheAppRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(openCacheAppService.getById(id));
  }

  @PostMapping("/save")
  public Result<Boolean> save(@RequestBody @Valid OpenCacheAppCreateDTO openCacheAppCreateDTO) {
    return Result.succeed(openCacheAppService.save(openCacheAppCreateDTO));
  }

  @PutMapping("/update")
  public Result<Boolean> update(@RequestBody @Valid OpenCacheAppUpdateDTO openCacheAppUpdateDTO) {
    return Result.succeed(openCacheAppService.updateById(openCacheAppUpdateDTO));
  }

  @PutMapping("/delete/{id}")
  public Result<Boolean> delete(@PathVariable("id") Long id) {
    return Result.succeed(openCacheAppService.deleteById(id));
  }

}
