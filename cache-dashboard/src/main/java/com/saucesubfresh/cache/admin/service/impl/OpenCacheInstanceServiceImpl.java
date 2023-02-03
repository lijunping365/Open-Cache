package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheAppService;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.common.time.LocalDateTimeUtil;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.store.InstanceStore;
import com.saucesubfresh.rpc.core.information.ServerInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:06
 */
@Service
public class OpenCacheInstanceServiceImpl implements OpenCacheInstanceService {

    private final InstanceStore instanceStore;
    private final OpenCacheAppService openCacheAppService;

    public OpenCacheInstanceServiceImpl(InstanceStore instanceStore,
                                        OpenCacheAppService openCacheAppService) {
        this.instanceStore = instanceStore;
        this.openCacheAppService = openCacheAppService;
    }

    @Override
    public PageResult<OpenCacheInstanceRespDTO> selectPage(OpenCacheInstanceReqDTO instanceReqDTO) {
        Long appId = instanceReqDTO.getAppId();
        OpenCacheAppRespDTO openCacheApp = openCacheAppService.getById(appId);
        List<ServerInformation> instances = instanceStore.getByNamespace(openCacheApp.getAppName());
        List<OpenCacheInstanceRespDTO> cacheInstance = convertList(instances);
        if (CollectionUtils.isEmpty(cacheInstance)){
            return PageResult.<OpenCacheInstanceRespDTO>newBuilder().build();
        }

        cacheInstance.sort(Comparator.comparing(OpenCacheInstanceRespDTO::getOnlineTime).reversed());
        return PageResult.build(cacheInstance, cacheInstance.size(), instanceReqDTO.getCurrent(), instanceReqDTO.getPageSize());
    }

    @Override
    public List<OpenCacheInstanceRespDTO> getInstanceList(Long appId) {
        OpenCacheAppRespDTO openJobApp = openCacheAppService.getById(appId);
        List<ServerInformation> instances = instanceStore.getByNamespace(openJobApp.getAppName());
        return convertList(instances);
    }

    private List<OpenCacheInstanceRespDTO> convertList(List<ServerInformation> instances) {
        if (CollectionUtils.isEmpty(instances)){
            return new ArrayList<>();
        }

        return instances.stream().map(e->{
            OpenCacheInstanceRespDTO instance = new OpenCacheInstanceRespDTO();
            instance.setServerId(e.getServerId());
            LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(e.getOnlineTime());
            instance.setOnlineTime(localDateTime);
            instance.setStatus(e.getStatus().name());
            instance.setWeight(e.getWeight());
            return instance;
        }).collect(Collectors.toList());
    }
}
