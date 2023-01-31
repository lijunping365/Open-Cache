package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.convert.OpenCacheReportConvert;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheReportRespDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheLogMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheReportMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Service
public class OpenCacheReportServiceImpl implements OpenCacheReportService {
    
    private final OpenCacheLogMapper openCacheLogMapper;
    private final OpenCacheReportMapper openCacheReportMapper;

    public OpenCacheReportServiceImpl(OpenCacheLogMapper openCacheLogMapper,
                                      OpenCacheReportMapper openCacheReportMapper) {
        this.openCacheLogMapper = openCacheLogMapper;
        this.openCacheReportMapper = openCacheReportMapper;
    }

    @Override
    public void insertReport() {
        int scheduleTotalCount = openCacheLogMapper.getScheduleTotalCount();
        int scheduleSucceedCount = openCacheLogMapper.getScheduleSucceedCount();
        OpenCacheReportDO openCacheReportDO = new OpenCacheReportDO();
        openCacheReportDO.setTaskExecTotalCount(scheduleTotalCount);
        openCacheReportDO.setTaskExecSuccessCount(scheduleSucceedCount);
        openCacheReportDO.setCreateTime(LocalDateTime.now());
        openCacheReportMapper.insert(openCacheReportDO);
    }

    @Override
    public List<OpenCacheReportRespDTO> getOpenCacheReportList() {
        List<OpenCacheReportDO> openCacheReportDOS = openCacheReportMapper.queryList();
        return OpenCacheReportConvert.INSTANCE.convertList(openCacheReportDOS);
    }

}
