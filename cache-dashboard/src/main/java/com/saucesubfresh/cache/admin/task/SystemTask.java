package com.saucesubfresh.cache.admin.task;

import com.saucesubfresh.cache.admin.mapper.OpenCacheMetricsMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.common.time.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/22
 */
@Component
public class SystemTask {

    @Value("${com.saucesubfresh.log.interval:7}")
    private Integer interval;

    private final OpenCacheMetricsMapper openCacheMetricsMapper;
    private final OpenCacheReportService openCacheReportService;

    public SystemTask(OpenCacheMetricsMapper openCacheMetricsMapper,
                      OpenCacheReportService openCacheReportService) {
        this.openCacheMetricsMapper = openCacheMetricsMapper;
        this.openCacheReportService = openCacheReportService;
    }


    /**
     * 每天 1：00：00 执行
     *
     * 1. 执行生成当日报表任务
     * 2. 执行清除n天前的任务日志
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void addReportTask(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.plusDays(-1);
        LocalDateTime time = LocalDateTimeUtil.getDayEnd(yesterday);
        openCacheReportService.generateReport(yesterday);
        openCacheMetricsMapper.clearLog(time, interval);
    }
}
