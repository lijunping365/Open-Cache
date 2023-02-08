package com.saucesubfresh.cache.admin.convert;

import com.saucesubfresh.cache.api.dto.create.OpenCacheLogCreateDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheLogRespDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Mapper
public interface OpenCacheLogConvert {

    OpenCacheLogConvert INSTANCE = Mappers.getMapper(OpenCacheLogConvert.class);

    OpenCacheLogRespDTO convert(OpenCacheLogDO OpenCacheLogDO);

    OpenCacheLogDO convert(OpenCacheLogCreateDTO OpenCacheLogCreateDTO);
}


