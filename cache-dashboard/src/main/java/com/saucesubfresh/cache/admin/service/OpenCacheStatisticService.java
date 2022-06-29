package com.saucesubfresh.cache.admin.service;


import com.saucesubfresh.cache.admin.dto.resp.OpenCacheStatisticNumberRespDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheStatisticReportRespDTO;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenCacheStatisticService {

    OpenCacheStatisticNumberRespDTO getStatisticNumber();

    List<OpenCacheStatisticReportRespDTO> getStatisticReport();
}