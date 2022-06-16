package com.saucesubfresh.cache.admin.convert;

import com.saucesubfresh.cache.admin.dto.create.OpenCacheAppCreateDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.admin.dto.update.OpenCacheAppUpdateDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Mapper
public interface OpenCacheAppConvert {

    OpenCacheAppConvert INSTANCE = Mappers.getMapper(OpenCacheAppConvert.class);

    OpenCacheAppRespDTO convert(OpenCacheAppDO OpenCacheAppDO);

    OpenCacheAppDO convert(OpenCacheAppCreateDTO OpenCacheAppCreateDTO);

    OpenCacheAppDO convert(OpenCacheAppUpdateDTO OpenCacheAppUpdateDTO);

    List<OpenCacheAppRespDTO> convertList(List<OpenCacheAppDO> openCacheAppDOS);
}


