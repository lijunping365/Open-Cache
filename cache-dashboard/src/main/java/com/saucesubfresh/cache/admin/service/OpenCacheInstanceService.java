package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.admin.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:05
 */
public interface OpenCacheInstanceService {
    
    PageResult<OpenCacheInstanceRespDTO> selectPage(OpenCacheInstanceReqDTO instanceReqDTO);

    Boolean offlineClient(String clientId);

    Boolean onlineClient(String clientId);
}
