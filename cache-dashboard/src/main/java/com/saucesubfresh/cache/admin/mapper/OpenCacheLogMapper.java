package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.entity.OpenCacheLogDO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheLogReqDTO;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * 缓存操作日志
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Repository
public interface OpenCacheLogMapper extends BaseMapper<OpenCacheLogDO> {

    default Page<OpenCacheLogDO> queryPage(OpenCacheLogReqDTO openCacheLogReqDTO){
        return selectPage(openCacheLogReqDTO.page(), Wrappers.<OpenCacheLogDO>lambdaQuery()
            .eq(Objects.nonNull(openCacheLogReqDTO.getAppId()), OpenCacheLogDO::getAppId, openCacheLogReqDTO.getAppId())
            .eq(Objects.nonNull(openCacheLogReqDTO.getCacheName()), OpenCacheLogDO::getCacheName, openCacheLogReqDTO.getCacheName())
            .eq(Objects.nonNull(openCacheLogReqDTO.getKey()), OpenCacheLogDO::getKey, openCacheLogReqDTO.getKey())
            .eq(Objects.nonNull(openCacheLogReqDTO.getCommand()), OpenCacheLogDO::getCommand, openCacheLogReqDTO.getCommand())
            .eq(Objects.nonNull(openCacheLogReqDTO.getStatus()), OpenCacheLogDO::getStatus, openCacheLogReqDTO.getStatus())
            .between(Objects.nonNull(openCacheLogReqDTO.getBeginTime()), OpenCacheLogDO::getCreateTime, openCacheLogReqDTO.getBeginTime(), openCacheLogReqDTO.getEndTime())
            .orderByDesc(OpenCacheLogDO::getCreateTime)
        );
    }
}
