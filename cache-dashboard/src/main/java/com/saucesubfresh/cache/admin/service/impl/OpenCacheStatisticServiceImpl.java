package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.common.OpenCacheReportEnum;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheReportRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticNumberRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticReportRespDTO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheLogMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.admin.service.OpenCacheStatisticService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/4/11
 */
@Service
public class OpenCacheStatisticServiceImpl implements OpenCacheStatisticService {

    private final OpenCacheLogMapper openCacheLogMapper;
    private final OpenCacheReportService openCacheReportService;

    public OpenCacheStatisticServiceImpl(OpenCacheLogMapper openCacheLogMapper,
                                         OpenCacheReportService openCacheReportService) {
        this.openCacheLogMapper = openCacheLogMapper;
        this.openCacheReportService = openCacheReportService;
    }

    @Override
    public OpenCacheStatisticNumberRespDTO getStatisticNumber() {
        int scheduleTotalCount = openCacheLogMapper.getScheduleTotalCount();
        int scheduleSucceedCount = openCacheLogMapper.getScheduleSucceedCount();

        OpenCacheStatisticNumberRespDTO numberRespDTO = new OpenCacheStatisticNumberRespDTO();
        numberRespDTO.setScheduleTotalNum(scheduleTotalCount);
        numberRespDTO.setScheduleSucceedNum(scheduleSucceedCount);
        return numberRespDTO;
    }

    @Override
    public List<OpenCacheStatisticReportRespDTO> getStatisticReport() {
        List<OpenCacheReportRespDTO> reportRespDTOS = openCacheReportService.getOpenCacheReportList();
        if (CollectionUtils.isEmpty(reportRespDTOS)){
            return Collections.emptyList();
        }

        List<OpenCacheStatisticReportRespDTO> openCacheStatisticReportRespDTOS = reportRespDTOS.stream().map(e -> {
            OpenCacheStatisticReportRespDTO openCacheStatisticReportRespDTO = new OpenCacheStatisticReportRespDTO();
            openCacheStatisticReportRespDTO.setDate(e.getCreateTime());
            openCacheStatisticReportRespDTO.setName(OpenCacheReportEnum.TASK_EXEC_TOTAL_COUNT.getName());
            openCacheStatisticReportRespDTO.setValue(e.getTaskExecTotalCount());
            return openCacheStatisticReportRespDTO;
        }).collect(Collectors.toList());

        final List<OpenCacheStatisticReportRespDTO> taskExecSuccessList = reportRespDTOS.stream().map(e -> {
            OpenCacheStatisticReportRespDTO openCacheStatisticReportRespDTO = new OpenCacheStatisticReportRespDTO();
            openCacheStatisticReportRespDTO.setDate(e.getCreateTime());
            openCacheStatisticReportRespDTO.setName(OpenCacheReportEnum.TASK_EXEC_SUCCESS_COUNT.getName());
            openCacheStatisticReportRespDTO.setValue(e.getTaskExecSuccessCount());
            return openCacheStatisticReportRespDTO;
        }).collect(Collectors.toList());
        openCacheStatisticReportRespDTOS.addAll(taskExecSuccessList);

        return openCacheStatisticReportRespDTOS;
    }
}
