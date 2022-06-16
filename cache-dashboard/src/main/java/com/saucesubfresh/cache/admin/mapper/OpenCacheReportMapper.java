package com.saucesubfresh.cache.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lijunping on 2022/4/11
 */
@Repository
public interface OpenCacheReportMapper extends BaseMapper<OpenCacheReportDO> {


    default List<OpenCacheReportDO> queryList(){
        return selectList(Wrappers.lambdaQuery());
    }
}
