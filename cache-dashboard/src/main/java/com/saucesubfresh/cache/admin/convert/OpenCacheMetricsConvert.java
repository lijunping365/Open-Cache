package com.saucesubfresh.cache.admin.convert;

import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author lijunping on 2022/4/12
 */
@Mapper
public interface OpenCacheMetricsConvert {

    OpenCacheMetricsConvert INSTANCE = Mappers.getMapper(OpenCacheMetricsConvert.class);

    List<OpenCacheMetricsRespDTO> convertList(List<OpenCacheMetricsDO> cacheMetricsDOS);

    OpenCacheMetricsRespDTO convert(OpenCacheMetricsDO openCacheMetricsDO);
}
