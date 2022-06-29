package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.admin.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.admin.service.OpenCacheAppService;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.common.time.LocalDateTimeUtil;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.core.information.ClientInformation;
import com.saucesubfresh.rpc.server.manager.InstanceManager;
import com.saucesubfresh.rpc.server.store.InstanceStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:06
 */
@Service
public class OpenCacheInstanceServiceImpl implements OpenCacheInstanceService {

    private final InstanceStore instanceStore;
    private final InstanceManager instanceManager;
    private final OpenCacheAppService openCacheAppService;

    public OpenCacheInstanceServiceImpl(InstanceStore instanceStore, InstanceManager instanceManager, OpenCacheAppService openCacheAppService) {
        this.instanceStore = instanceStore;
        this.instanceManager = instanceManager;
        this.openCacheAppService = openCacheAppService;
    }

    @Override
    public PageResult<OpenCacheInstanceRespDTO> selectPage(OpenCacheInstanceReqDTO instanceReqDTO) {
        final OpenCacheAppRespDTO openCacheApp = openCacheAppService.getById(instanceReqDTO.getAppId());
        List<ClientInformation> instances = instanceStore.getByNamespace(openCacheApp.getAppName());
        List<OpenCacheInstanceRespDTO> CacheInstance = convertList(instances);
        return PageResult.<OpenCacheInstanceRespDTO>newBuilder()
                .records(CacheInstance)
                .total((long) CacheInstance.size())
                .current(1L)
                .pages((long) (CacheInstance.size() / 10))
                .build();
    }

    @Override
    public Boolean offlineClient(String clientId) {
        return instanceManager.offlineClient(clientId);
    }

    @Override
    public Boolean onlineClient(String clientId) {
        return instanceManager.onlineClient(clientId);
    }

    private List<OpenCacheInstanceRespDTO> convertList(List<ClientInformation> instances) {
        if (CollectionUtils.isEmpty(instances)){
            return new ArrayList<>();
        }

        return instances.stream().map(e->{
            OpenCacheInstanceRespDTO instance = new OpenCacheInstanceRespDTO();
            instance.setClientId(e.getClientId());
            LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(e.getOnlineTime());
            instance.setOnlineTime(localDateTime);
            instance.setStatus(e.getStatus());
            instance.setWeight(e.getWeight());
            return instance;
        }).collect(Collectors.toList());
    }
}