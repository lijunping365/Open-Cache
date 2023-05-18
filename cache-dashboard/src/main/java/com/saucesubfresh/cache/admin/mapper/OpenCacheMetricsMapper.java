package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenCacheMetricsMapper extends BaseMapper<OpenCacheMetricsDO> {

    default Page<OpenCacheMetricsDO> queryPage(OpenCacheMetricsReqDTO metricsReqDTO){
        return selectPage(metricsReqDTO.page(), Wrappers.<OpenCacheMetricsDO>lambdaQuery()
                .eq(Objects.nonNull(metricsReqDTO.getAppId()), OpenCacheMetricsDO::getAppId, metricsReqDTO.getAppId())
                .eq(Objects.nonNull(metricsReqDTO.getCacheName()), OpenCacheMetricsDO::getCacheName, metricsReqDTO.getCacheName())
                .between(Objects.nonNull(metricsReqDTO.getBeginTime()), OpenCacheMetricsDO::getCreateTime, metricsReqDTO.getBeginTime(), metricsReqDTO.getEndTime())
                .orderByDesc(OpenCacheMetricsDO::getCreateTime)
        );
    }

    default List<OpenCacheMetricsDO> queryList(Long appId, String instanceId, String cacheName, Integer count){
        return selectList(Wrappers.<OpenCacheMetricsDO>lambdaQuery()
                .eq(OpenCacheMetricsDO::getAppId, appId)
                .eq(StringUtils.isNotBlank(instanceId), OpenCacheMetricsDO::getInstanceId, instanceId)
                .eq(StringUtils.isNotBlank(cacheName), OpenCacheMetricsDO::getCacheName, cacheName)
                .orderByDesc(OpenCacheMetricsDO::getCreateTime)
                .last("limit " + count)
        );
    }
}
