package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.api.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 查询全部客户端示例
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult<OpenCacheInstanceRespDTO>> getAllInstance(OpenCacheInstanceReqDTO instanceReqDTO){
        return Result.succeed(instanceService.selectPage(instanceReqDTO));
    }

    /**
     * 客户端下线
     * @return
     */
    @PutMapping("/offline/{serverId}")
    public Result<Boolean> offlineInstance(@PathVariable("serverId") String serverId){
        return Result.succeed(instanceService.offlineServer(serverId));
    }

    /**
     * 客户端上线
     * @return
     */
    @PutMapping("/online/{serverId}")
    public Result<Boolean> onlineInstance(@PathVariable("serverId") String serverId){
        return Result.succeed(instanceService.onlineServer(serverId));
    }
}
