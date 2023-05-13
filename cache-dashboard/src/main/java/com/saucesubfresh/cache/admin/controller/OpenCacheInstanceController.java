package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 集群节点管理端点
 *
 * @author lijunping on 2022/2/16
 */
@Validated
@RestController
@RequestMapping("/instance")
public class OpenCacheInstanceController {

    @Autowired
    private OpenCacheInstanceService instanceService;

    /**
     * 分页查询执行器列表
     */
    @GetMapping("/page")
    public Result<PageResult<OpenCacheInstanceRespDTO>> getPage(OpenCacheInstanceReqDTO instanceReqDTO){
        return Result.succeed(instanceService.selectPage(instanceReqDTO));
    }

    /**
     * 查询全部执行器列表
     */
    @GetMapping("/list")
    public Result<List<OpenCacheInstanceRespDTO>> getAllInstance(@RequestParam("appId") Long appId){
        return Result.succeed(instanceService.getInstanceList(appId));
    }
}
