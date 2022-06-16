package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.common.enums.SystemScheduleEnum;
import com.saucesubfresh.cache.admin.convert.OpenCacheReportConvert;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheReportRespDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheLogMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheReportMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.starter.schedule.core.ScheduleTaskManage;
import com.saucesubfresh.starter.schedule.domain.ScheduleTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Service
public class OpenCacheReportServiceImpl implements OpenCacheReportService {

    @Value("${report-cron-express}")
    private String reportCronExpress;
    
    private final OpenCacheLogMapper openCacheLogMapper;
    private final OpenCacheReportMapper openCacheReportMapper;
    private final ScheduleTaskManage scheduleTaskManage;

    public OpenCacheReportServiceImpl(OpenCacheLogMapper openCacheLogMapper,
                                      OpenCacheReportMapper openCacheReportMapper,
                                      ScheduleTaskManage scheduleTaskManage) {
        this.openCacheLogMapper = openCacheLogMapper;
        this.openCacheReportMapper = openCacheReportMapper;
        this.scheduleTaskManage = scheduleTaskManage;
    }

    @PostConstruct
    public void addReportTask(){
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setTaskId(SystemScheduleEnum.REPORT.getValue());
        scheduleTask.setCronExpression(reportCronExpress);
        scheduleTaskManage.addScheduleTask(scheduleTask);
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
