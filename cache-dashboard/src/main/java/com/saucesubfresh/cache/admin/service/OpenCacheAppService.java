package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.api.dto.create.OpenCacheAppCreateDTO;
import com.saucesubfresh.cache.api.dto.del.DeleteDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheAppReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheAppUpdateDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

import java.util.List;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
public interface OpenCacheAppService {

    PageResult<OpenCacheAppRespDTO> selectPage(OpenCacheAppReqDTO openCacheAppReqDTO);

    OpenCacheAppRespDTO getById(Long id);

    boolean save(OpenCacheAppCreateDTO openCacheAppCreateDTO);

    boolean updateById(OpenCacheAppUpdateDTO openCacheAppUpdateDTO);

    boolean deleteBatchIds(DeleteDTO deleteDTO);

    List<OpenCacheAppRespDTO> queryList(String appName);
}

