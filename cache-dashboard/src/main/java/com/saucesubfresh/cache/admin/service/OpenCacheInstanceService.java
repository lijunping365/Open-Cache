package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:05
 */
public interface OpenCacheInstanceService {
    
    PageResult<OpenCacheInstanceRespDTO> selectPage(OpenCacheInstanceReqDTO instanceReqDTO);

    OpenCacheInstanceRespDTO getInstanceById(Long appId, String serverId);

    List<OpenCacheInstanceRespDTO> getInstanceList(Long appId);
}
