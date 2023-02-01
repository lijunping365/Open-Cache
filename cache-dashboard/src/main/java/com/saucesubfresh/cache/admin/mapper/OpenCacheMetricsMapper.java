package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenCacheMetricsMapper extends BaseMapper<OpenCacheMetricsDO> {


    default List<OpenCacheMetricsDO> queryList(){
        return selectList(Wrappers.lambdaQuery());
    }
}
