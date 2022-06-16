package com.saucesubfresh.cache.admin.service;

import com.saucesubfresh.cache.admin.dto.resp.OpenCacheReportRespDTO;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
public interface OpenCacheReportService {

    void insertReport();

    List<OpenCacheReportRespDTO> getOpenCacheReportList();
}
