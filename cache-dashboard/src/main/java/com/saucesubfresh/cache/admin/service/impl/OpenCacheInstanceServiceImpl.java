package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheAppService;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheInstanceReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheAppRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageResponse;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.json.JSON;
import com.saucesubfresh.cache.common.metrics.SystemMetricsInfo;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.time.LocalDateTimeUtil;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.remoting.RemotingInvoker;
import com.saucesubfresh.rpc.client.store.InstanceStore;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.constants.CommonConstant;
import com.saucesubfresh.rpc.core.enums.ResponseStatus;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.core.information.ServerInformation;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:06
 */
@Slf4j
@Service
public class OpenCacheInstanceServiceImpl implements OpenCacheInstanceService {

    /**
     *  12.3%(4 cores)
     */
    private static final String CPU_FORMAT = "%s / %s cores";
    /**
     *  27.7%(2.9/8.0 GB)
     */
    private static final String MEMORY_FORMAT = "%s%%（%s / %s GB）";

    private static final DecimalFormat df = new DecimalFormat("#.#");

    private final InstanceStore instanceStore;
    private final RemotingInvoker remotingInvoker;
    private final OpenCacheAppService openCacheAppService;

    public OpenCacheInstanceServiceImpl(InstanceStore instanceStore,
                                        RemotingInvoker remotingInvoker,
                                        OpenCacheAppService openCacheAppService) {
        this.instanceStore = instanceStore;
        this.remotingInvoker = remotingInvoker;
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

        int totalSize = cacheInstance.size();
        Integer pageNum = instanceReqDTO.getCurrent();
        Integer pageSize = instanceReqDTO.getPageSize();

        //进行分页处理
        if ((pageNum -1) * pageSize >= totalSize){
            return PageResult.<OpenCacheInstanceRespDTO>newBuilder().build();
        }
        int endIndex = Math.min(totalSize, pageNum * pageSize);

        List<OpenCacheInstanceRespDTO> respDTOS = new ArrayList<>();
        for (int i = (pageNum -1) * pageSize; i < endIndex; i++) {
            respDTOS.add(cacheInstance.get(i));
        }

        for (OpenCacheInstanceRespDTO instance : respDTOS) {
            doWrapper(instance);
        }

        return PageResult.build(cacheInstance, cacheInstance.size(), instanceReqDTO.getCurrent(), instanceReqDTO.getPageSize());
    }

    @Override
    public OpenCacheInstanceRespDTO getInstanceById(Long appId, String serverId) {
        OpenCacheAppRespDTO openCacheApp = openCacheAppService.getById(appId);
        List<ServerInformation> instances = instanceStore.getByNamespace(openCacheApp.getAppName());
        if (CollectionUtils.isEmpty(instances)){
            return null;
        }

        ServerInformation serverInformation = instances.stream()
                .filter(e -> StringUtils.equals(e.getServerId(), serverId))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(serverInformation)){
            return null;
        }

        List<OpenCacheInstanceRespDTO> openJobInstanceRespDTOS = convertList(Collections.singletonList(serverInformation));
        OpenCacheInstanceRespDTO openJobInstanceRespDTO = openJobInstanceRespDTOS.get(0);
        doWrapper(openJobInstanceRespDTO);
        return openJobInstanceRespDTO;
    }

    @Override
    public List<OpenCacheInstanceRespDTO> getInstanceList(Long appId) {
        OpenCacheAppRespDTO openJobApp = openCacheAppService.getById(appId);
        List<ServerInformation> instances = instanceStore.getByNamespace(openJobApp.getAppName());
        return convertList(instances);
    }

    private MessageResponseBody doInvoke(Message message, ServerInformation serverInformation){
        MessageResponseBody response;
        try {
            response = remotingInvoker.invoke(message, serverInformation);
        }catch (RpcException e){
            throw new RpcException(e.getMessage());
        }
        if (Objects.nonNull(response) && response.getStatus() != ResponseStatus.SUCCESS){
            throw new RpcException("处理失败");
        }
        return response;
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
            LocalDateTime now = LocalDateTime.now();
            instance.setLiveTime(LocalDateTimeUtil.getTimeBetween(localDateTime, now));
            instance.setStatus(e.getStatus().name());
            instance.setWeight(e.getWeight());
            return instance;
        }).collect(Collectors.toList());
    }

    private void doWrapper(OpenCacheInstanceRespDTO instance){
        Message message = new Message();
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCommand(CacheCommandEnum.QUERY_NODE_METRICS.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        String[] serverIdArray = instance.getServerId().split(CommonConstant.Symbol.MH);
        ServerInformation serverInformation = new ServerInformation(serverIdArray[0], Integer.parseInt(serverIdArray[1]));

        MessageResponseBody messageResponseBody;
        try {
            messageResponseBody = doInvoke(message, serverInformation);
        }catch (RpcException ex){
            log.error(ex.getMessage(), ex);
            return;
        }

        byte[] body = messageResponseBody.getBody();
        CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);
        if (StringUtils.isBlank(response.getData())){
            return;
        }

        SystemMetricsInfo metricsInfo = JSON.parse(response.getData(), SystemMetricsInfo.class);
        wrapperMetricsInfo(instance, metricsInfo);
    }

    private void wrapperMetricsInfo(OpenCacheInstanceRespDTO instance, SystemMetricsInfo metricsInfo){
        if (Objects.isNull(metricsInfo)){
            return;
        }
        // CPU 指标
        String cpuInfo = String.format(CPU_FORMAT, df.format(metricsInfo.getCpuLoad()), metricsInfo.getCpuProcessors());
        // 内存指标
        String menU = df.format(metricsInfo.getJvmMemoryUsage() * 100);
        String menUsed = df.format(metricsInfo.getJvmUsedMemory());
        String menMax = df.format(metricsInfo.getJvmMaxMemory());
        String memoryInfo = String.format(MEMORY_FORMAT, menU, menUsed, menMax);
        // 磁盘指标
        String diskU = df.format(metricsInfo.getDiskUsage() * 100);
        String diskUsed = df.format(metricsInfo.getDiskUsed());
        String diskMax = df.format(metricsInfo.getDiskTotal());
        String diskInfo = String.format(MEMORY_FORMAT, diskU, diskUsed, diskMax);

        instance.setCpuInfo(cpuInfo);
        instance.setMemoryInfo(memoryInfo);
        instance.setDiskInfo(diskInfo);
    }
}
