package com.saucesubfresh.cache.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saucesubfresh.cache.admin.convert.OpenCacheAppConvert;
import com.saucesubfresh.cache.api.dto.create.OpenCacheAppCreateDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheAppReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.update.OpenCacheAppUpdateDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheAppService;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.discovery.ServiceDiscovery;
import com.saucesubfresh.rpc.client.namespace.NamespaceService;
import com.saucesubfresh.starter.security.context.UserSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OpenCacheAppServiceImpl extends ServiceImpl<OpenCacheAppMapper, OpenCacheAppDO> implements OpenCacheAppService, NamespaceService {

    private final OpenCacheAppMapper openCacheAppMapper;
    private final ServiceDiscovery serviceDiscovery;

    public OpenCacheAppServiceImpl(OpenCacheAppMapper openCacheAppMapper, ServiceDiscovery serviceDiscovery) {
        this.openCacheAppMapper = openCacheAppMapper;
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public PageResult<OpenCacheAppRespDTO> selectPage(OpenCacheAppReqDTO openCacheAppReqDTO) {
        Page<OpenCacheAppDO> page = openCacheAppMapper.queryPage(openCacheAppReqDTO);
        IPage<OpenCacheAppRespDTO> convert = page.convert(OpenCacheAppConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenCacheAppRespDTO getById(Long id) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(id);
        return OpenCacheAppConvert.INSTANCE.convert(openCacheAppDO);
    }

    @Override
    public boolean save(OpenCacheAppCreateDTO openCacheAppCreateDTO) {
        OpenCacheAppDO openCacheAppDO = OpenCacheAppConvert.INSTANCE.convert(openCacheAppCreateDTO);
        openCacheAppDO.setCreateTime(LocalDateTime.now());
        openCacheAppDO.setCreateUser(UserSecurityContextHolder.getUserId());
        openCacheAppMapper.insert(openCacheAppDO);
        this.startSubscribe();
        return true;
    }

    @Override
    public boolean updateById(OpenCacheAppUpdateDTO openCacheAppUpdateDTO) {
        openCacheAppMapper.updateById(OpenCacheAppConvert.INSTANCE.convert(openCacheAppUpdateDTO));
        this.startSubscribe();
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        openCacheAppMapper.deleteById(id);
        this.startSubscribe();
        return true;
    }

    @Override
    public List<OpenCacheAppRespDTO> queryList(String appName) {
        List<OpenCacheAppDO> openCacheAppDOS = openCacheAppMapper.queryList(appName);
        return OpenCacheAppConvert.INSTANCE.convertList(openCacheAppDOS);
    }

    @PostConstruct
    public void startSubscribe() {
        List<OpenCacheAppDO> openCacheAppDOS = openCacheAppMapper.selectList(Wrappers.lambdaQuery());
        if (CollectionUtils.isEmpty(openCacheAppDOS)){
            return;
        }
        List<String> namespaces = openCacheAppDOS.stream().map(OpenCacheAppDO::getAppName).collect(Collectors.toList());
        serviceDiscovery.subscribe(namespaces);
    }

    @Override
    public List<String> loadNamespace() {
        List<OpenCacheAppDO> openCacheAppDOS = openCacheAppMapper.selectList(Wrappers.lambdaQuery());
        if (CollectionUtils.isEmpty(openCacheAppDOS)){
            return Collections.emptyList();
        }
        return openCacheAppDOS.stream().map(OpenCacheAppDO::getAppName).collect(Collectors.toList());
    }
}