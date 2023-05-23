package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    default List<OpenCacheMetricsDO> queryList(Long appId, String cacheName, String instanceId, LocalDateTime startTime, LocalDateTime endTime){
        return selectList(Wrappers.<OpenCacheMetricsDO>lambdaQuery()
                .eq(OpenCacheMetricsDO::getAppId, appId)
                .eq(OpenCacheMetricsDO::getCacheName, cacheName)
                .eq(OpenCacheMetricsDO::getInstanceId, instanceId)
                .between(OpenCacheMetricsDO::getCreateTime, startTime, endTime)
                .orderByDesc(OpenCacheMetricsDO::getCreateTime)
        );
    }

    default void clearMetrics(LocalDateTime time, Integer interval){
        delete(Wrappers.<OpenCacheMetricsDO>lambdaQuery()
                .lt(OpenCacheMetricsDO::getCreateTime, time.plusDays(-interval))
        );
    }

    List<OpenCacheMetricsDO> groupByAppId(@Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    List<OpenCacheMetricsDO> groupByCacheName(@Param("appId") Long appId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    List<OpenCacheMetricsDO> groupByInstanceId(@Param("appId") Long appId,
                                               @Param("cacheName") String cacheName,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}
