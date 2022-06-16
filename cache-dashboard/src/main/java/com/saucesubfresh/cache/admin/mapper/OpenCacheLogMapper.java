package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheLogDO;
import com.saucesubfresh.cache.common.enums.CommonStatusEnum;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 任务运行日志
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Repository
public interface OpenCacheLogMapper extends BaseMapper<OpenCacheLogDO> {

    default Page<OpenCacheLogDO> queryPage(OpenCacheLogReqDTO openCacheLogReqDTO){
        return selectPage(openCacheLogReqDTO.page(), Wrappers.<OpenCacheLogDO>lambdaQuery()
            .eq(Objects.nonNull(openCacheLogReqDTO.getCacheId()), OpenCacheLogDO::getCacheId, openCacheLogReqDTO.getCacheId())
            .eq(Objects.nonNull(openCacheLogReqDTO.getStatus()), OpenCacheLogDO::getStatus, openCacheLogReqDTO.getStatus())
            .between(Objects.nonNull(openCacheLogReqDTO.getBeginTime()), OpenCacheLogDO::getCreateTime, openCacheLogReqDTO.getBeginTime(), openCacheLogReqDTO.getEndTime())
            .orderByDesc(OpenCacheLogDO::getCreateTime)
        );
    }

    default int getScheduleTotalCount(){
        return selectCount(Wrappers.lambdaQuery());
    }

    default int getScheduleSucceedCount(){
        return selectCount(Wrappers.<OpenCacheLogDO>lambdaQuery()
                .eq(OpenCacheLogDO::getStatus, CommonStatusEnum.YES.getValue())
        );
    }

    default void clearLog(Integer interval){
        delete(Wrappers.<OpenCacheLogDO>lambdaQuery()
                .lt(OpenCacheLogDO::getCreateTime, LocalDateTime.now().plusDays(-interval))
        );
    }
}
