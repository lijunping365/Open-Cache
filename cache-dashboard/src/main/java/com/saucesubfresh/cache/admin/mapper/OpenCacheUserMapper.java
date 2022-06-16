package com.saucesubfresh.cache.admin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.dto.req.OpenCacheUserReqDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheUserDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 用户表
 * 
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-06-22 15:20:30
 */
@Repository
public interface OpenCacheUserMapper extends BaseMapper<OpenCacheUserDO> {

    default OpenCacheUserDO loadUserByUsername(String username){
        return selectOne(Wrappers.<OpenCacheUserDO>lambdaQuery()
                .eq(OpenCacheUserDO::getUsername, username)
                .last("limit 1")
        );
    }

    default OpenCacheUserDO loadUserByMobile(String mobile){
        return selectOne(Wrappers.<OpenCacheUserDO>lambdaQuery()
                .eq(OpenCacheUserDO::getPhone, mobile)
                .last("limit 1")
        );
    }

    default Page<OpenCacheUserDO> queryPage(OpenCacheUserReqDTO openCacheUserReqDTO){
        return selectPage(openCacheUserReqDTO.page(), Wrappers.<OpenCacheUserDO>lambdaQuery()
                .like(StringUtils.isNotBlank(openCacheUserReqDTO.getUsername()), OpenCacheUserDO::getUsername, openCacheUserReqDTO.getUsername())
                .eq(StringUtils.isNotBlank(openCacheUserReqDTO.getPhone()), OpenCacheUserDO::getPhone, openCacheUserReqDTO.getPhone())
        );
    }
}
