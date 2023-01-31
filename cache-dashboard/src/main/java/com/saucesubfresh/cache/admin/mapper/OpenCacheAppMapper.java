package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.api.dto.req.OpenCacheAppReqDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Repository
public interface OpenCacheAppMapper extends BaseMapper<OpenCacheAppDO> {

    default Page<OpenCacheAppDO> queryPage(OpenCacheAppReqDTO openCacheAppReqDTO){
        return selectPage(openCacheAppReqDTO.page(), Wrappers.<OpenCacheAppDO>lambdaQuery()
                .like(Objects.nonNull(openCacheAppReqDTO.getAppDesc()), OpenCacheAppDO::getAppDesc, openCacheAppReqDTO.getAppDesc())
                .like(Objects.nonNull(openCacheAppReqDTO.getAppName()), OpenCacheAppDO::getAppName, openCacheAppReqDTO.getAppName())
                .between(Objects.nonNull(openCacheAppReqDTO.getBeginTime()), OpenCacheAppDO::getCreateTime, openCacheAppReqDTO.getBeginTime(), openCacheAppReqDTO.getEndTime()));
    }

    default List<OpenCacheAppDO> queryList(String appName){
        return selectList(Wrappers.<OpenCacheAppDO>lambdaQuery()
                .like(StringUtils.isNotBlank(appName), OpenCacheAppDO::getAppName, appName)
        );
    }
}
