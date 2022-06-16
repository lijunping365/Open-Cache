package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.admin.dto.create.OpenCacheAppCreateDTO;
import com.saucesubfresh.cache.admin.dto.req.OpenCacheAppReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.admin.dto.update.OpenCacheAppUpdateDTO;
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

    boolean deleteById(Long id);

    List<OpenCacheAppRespDTO> queryList(String appName);
}

