package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenCacheReportMapper extends BaseMapper<OpenCacheReportDO> {

    default List<OpenCacheReportDO> queryList(Long appId, String cacheName, String instanceId, LocalDateTime beginTime, LocalDateTime endTime){
        return selectList(Wrappers.<OpenCacheReportDO>lambdaQuery()
                .eq(OpenCacheReportDO::getAppId, appId)
                .eq(StringUtils.isNotBlank(cacheName), OpenCacheReportDO::getCacheName, cacheName)
                .eq(StringUtils.isNotBlank(instanceId), OpenCacheReportDO::getInstanceId, instanceId)
                .between(Objects.nonNull(beginTime), OpenCacheReportDO::getCreateTime, beginTime, endTime)
                .orderByDesc(OpenCacheReportDO::getCreateTime)
        );
    }
}
