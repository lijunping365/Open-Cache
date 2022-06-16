package com.saucesubfresh.cache.admin.service;


import com.saucesubfresh.cache.admin.dto.create.OpenCacheUserCreateDTO;
import com.saucesubfresh.cache.admin.dto.req.OpenCacheUserReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheUserRespDTO;
import com.saucesubfresh.cache.admin.dto.update.OpenCacheUserUpdateDTO;
import com.saucesubfresh.cache.common.vo.PageResult;

/**
 * 用户表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
public interface OpenCacheUserService {

    PageResult<OpenCacheUserRespDTO> selectPage(OpenCacheUserReqDTO openCacheUserReqDTO);

    OpenCacheUserRespDTO getById(Long id);

    boolean save(OpenCacheUserCreateDTO openCacheUserCreateDTO);

    boolean updateById(OpenCacheUserUpdateDTO openCacheUserUpdateDTO);

    boolean deleteById(Long id);

    OpenCacheUserRespDTO loadUserByUserId(Long userId);
}

