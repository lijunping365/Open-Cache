package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.api.dto.create.OpenCacheUserCreateDTO;
import com.saucesubfresh.cache.api.dto.del.DeleteDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUserReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheUserRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheUserUpdateDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheUserService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 用户表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
@Validated
@RestController
@RequestMapping("/user")
public class OpenCacheUserController {

  @Autowired
  private OpenCacheUserService openCacheUserService;

  @GetMapping("/currentUser")
  public Result<OpenCacheUserRespDTO> getCurrentUser() {
    Long userId = UserSecurityContextHolder.getUserId();
    return Result.succeed(openCacheUserService.loadUserByUserId(userId));
  }

  @GetMapping("/page")
  public Result<PageResult<OpenCacheUserRespDTO>> page(OpenCacheUserReqDTO openCacheUserReqDTO) {
    return Result.succeed(openCacheUserService.selectPage(openCacheUserReqDTO));
  }

  @GetMapping("/info/{id}")
  public Result<OpenCacheUserRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(openCacheUserService.getById(id));
  }

  @PostMapping("/save")
  public Result<Boolean> save(@RequestBody @Valid OpenCacheUserCreateDTO openCacheUserCreateDTO) {
    return Result.succeed(openCacheUserService.save(openCacheUserCreateDTO));
  }

  @PutMapping("/update")
  public Result<Boolean> update(@RequestBody @Valid OpenCacheUserUpdateDTO openCacheUserUpdateDTO) {
    return Result.succeed(openCacheUserService.updateById(openCacheUserUpdateDTO));
  }

  @DeleteMapping("/delete")
  public Result<Boolean> delete(@RequestBody @Valid DeleteDTO deleteDTO) {
    return Result.succeed(openCacheUserService.deleteBatchIds(deleteDTO));
  }

}
