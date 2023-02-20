package com.saucesubfresh.cache.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saucesubfresh.cache.admin.convert.OpenCacheUserConvert;
import com.saucesubfresh.cache.api.dto.create.OpenCacheUserCreateDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUserReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheUserRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheUserUpdateDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheUserDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheUserMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheUserService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.starter.oauth.domain.UserDetails;
import com.saucesubfresh.starter.oauth.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class OpenCacheUserServiceImpl extends ServiceImpl<OpenCacheUserMapper, OpenCacheUserDO> implements OpenCacheUserService, UserDetailService {

    @Autowired
    private OpenCacheUserMapper openCacheUserMapper;

    @Override
    public PageResult<OpenCacheUserRespDTO> selectPage(OpenCacheUserReqDTO openCacheUserReqDTO) {
        Page<OpenCacheUserDO> page = openCacheUserMapper.queryPage(openCacheUserReqDTO);
        IPage<OpenCacheUserRespDTO> convert = page.convert(OpenCacheUserConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenCacheUserRespDTO getById(Long id) {
        OpenCacheUserDO openCacheUserDO = openCacheUserMapper.selectById(id);
        return OpenCacheUserConvert.INSTANCE.convert(openCacheUserDO);
    }

    @Override
    public boolean save(OpenCacheUserCreateDTO openCacheUserCreateDTO) {
        openCacheUserMapper.insert(OpenCacheUserConvert.INSTANCE.convert(openCacheUserCreateDTO));
        return true;
    }

    @Override
    public boolean updateById(OpenCacheUserUpdateDTO openCacheUserUpdateDTO) {
        openCacheUserMapper.updateById(OpenCacheUserConvert.INSTANCE.convert(openCacheUserUpdateDTO));
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        openCacheUserMapper.deleteById(id);
        return true;
    }

    @Override
    public OpenCacheUserRespDTO loadUserByUserId(Long userId) {
        OpenCacheUserDO openCacheUserDO = this.openCacheUserMapper.selectById(userId);
        return OpenCacheUserConvert.INSTANCE.convert(openCacheUserDO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        OpenCacheUserDO openCacheUserDO = this.openCacheUserMapper.loadUserByUsername(username);
        return convert(openCacheUserDO);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) {
        OpenCacheUserDO openCacheUserDO = this.openCacheUserMapper.loadUserByMobile(mobile);
        return convert(openCacheUserDO);
    }

    private UserDetails convert(OpenCacheUserDO openCacheUserDO){
        if (Objects.isNull(openCacheUserDO)){
            return null;
        }
        UserDetails userDetails = new UserDetails();
        userDetails.setId(openCacheUserDO.getId());
        userDetails.setUsername(openCacheUserDO.getUsername());
        userDetails.setPassword(openCacheUserDO.getPassword());
        userDetails.setMobile(openCacheUserDO.getPhone());
        userDetails.setAccountLocked(openCacheUserDO.getStatus() == 1);
        return userDetails;
    }
}