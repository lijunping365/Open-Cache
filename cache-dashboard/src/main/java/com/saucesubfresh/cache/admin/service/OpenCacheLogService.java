package com.saucesubfresh.cache.admin.service;


import com.saucesubfresh.cache.api.dto.create.OpenCacheLogCreateDTO;
import com.saucesubfresh.cache.api.dto.del.DeleteDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheLogRespDTO;
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

    boolean deleteBatchIds(DeleteDTO deleteDTO);
}

