package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenCacheReportMapper extends BaseMapper<OpenCacheReportDO> {

    default List<OpenCacheReportDO> queryList(Long appId, String cacheName, String instanceId, Integer count){
        return selectList(Wrappers.<OpenCacheReportDO>lambdaQuery()
                .eq(OpenCacheReportDO::getAppId, appId)
                .eq(StringUtils.isNotBlank(cacheName), OpenCacheReportDO::getCacheName, cacheName)
                .eq(StringUtils.isNotBlank(instanceId), OpenCacheReportDO::getInstanceId, instanceId)
                .orderByDesc(OpenCacheReportDO::getCreateTime)
                .last("limit " + count)
        );
    }
}
