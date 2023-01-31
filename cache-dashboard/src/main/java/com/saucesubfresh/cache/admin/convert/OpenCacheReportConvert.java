package com.saucesubfresh.cache.admin.convert;

import com.saucesubfresh.cache.api.dto.resp.OpenCacheReportRespDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author lijunping on 2022/4/12
 */
@Mapper
public interface OpenCacheReportConvert {

    OpenCacheReportConvert INSTANCE = Mappers.getMapper(OpenCacheReportConvert.class);

    List<OpenCacheReportRespDTO> convertList(List<OpenCacheReportDO> crawlerReportDOS);
}
