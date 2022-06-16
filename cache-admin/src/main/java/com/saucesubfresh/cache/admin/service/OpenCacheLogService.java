package com.saucesubfresh.cache.admin.service;


import com.saucesubfresh.cache.admin.dto.create.OpenCacheLogCreateDTO;
import com.saucesubfresh.cache.admin.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheLogRespDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
public interface OpenCacheLogService {

    PageResult<OpenCacheLogRespDTO> selectPage(OpenCacheLogReqDTO OpenCacheLogReqDTO);

    OpenCacheLogRespDTO getById(Long id);

    void save(OpenCacheLogCreateDTO OpenCacheLogCreateDTO);

    boolean deleteById(Long id);
}

