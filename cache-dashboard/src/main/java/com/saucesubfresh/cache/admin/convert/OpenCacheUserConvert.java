package com.saucesubfresh.cache.admin.convert;


import com.saucesubfresh.cache.api.dto.create.OpenCacheUserCreateDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheUserRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheUserUpdateDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 用户表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
@Mapper
public interface OpenCacheUserConvert {

    OpenCacheUserConvert INSTANCE = Mappers.getMapper(OpenCacheUserConvert.class);

    OpenCacheUserRespDTO convert(OpenCacheUserDO openCacheUserDO);

    OpenCacheUserDO convert(OpenCacheUserCreateDTO openCacheUserCreateDTO);

    OpenCacheUserDO convert(OpenCacheUserUpdateDTO openCacheUserUpdateDTO);

}


