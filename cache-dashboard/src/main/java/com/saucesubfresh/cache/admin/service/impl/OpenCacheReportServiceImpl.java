package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheMetricsMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageResponse;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import com.saucesubfresh.rpc.client.store.InstanceStore;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.enums.ResponseStatus;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.core.information.ServerInformation;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/4/11
 */
@Service
public class OpenCacheReportServiceImpl implements OpenCacheReportService {

    private final InstanceStore instanceStore;
    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper cacheAppMapper;
    private final OpenCacheMetricsMapper metricsMapper;

    public OpenCacheReportServiceImpl(InstanceStore instanceStore,
                                      ClusterInvoker clusterInvoker,
                                      OpenCacheAppMapper cacheAppMapper,
                                      OpenCacheMetricsMapper metricsMapper) {
        this.instanceStore = instanceStore;
        this.clusterInvoker = clusterInvoker;
        this.cacheAppMapper = cacheAppMapper;
        this.metricsMapper = metricsMapper;
    }

    @Override
    public OpenCacheStatisticRespDTO getStatistic(Long appId) {
        OpenCacheStatisticRespDTO respDTO = new OpenCacheStatisticRespDTO();
        OpenCacheAppDO openCacheAppDO = cacheAppMapper.selectById(appId);
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_NAMES_COUNT.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        MessageResponseBody responseBody;
        try {
            responseBody = doInvoke(message);
        }catch (RpcException ex){
            throw new ServiceException(ex.getMessage());
        }

        byte[] body = responseBody.getBody();
        CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);
        if (StringUtils.isNotBlank(response.getData())){
            respDTO.setCacheNameCount(Integer.parseInt(response.getData()));
        }
        List<ServerInformation> instances = instanceStore.getByNamespace(openCacheAppDO.getAppName());
        respDTO.setNodeCount(instances.size());
        return respDTO;
    }

    @Override
    public List<OpenCacheChartRespDTO> getChart(Long appId, Integer count) {
        List<OpenCacheMetricsDO> openJobReportDOS = metricsMapper.queryList(appId, count);
        if (CollectionUtils.isEmpty(openJobReportDOS)){
            return Collections.emptyList();
        }
        return openJobReportDOS.stream().map(e->{
            OpenCacheChartRespDTO openJobChartRespDTO = new OpenCacheChartRespDTO();
            openJobChartRespDTO.setDate(e.getCreateTime().toLocalDate());
            openJobChartRespDTO.setHitCount(e.getRequestCount());
            openJobChartRespDTO.setHitCount(e.getHitCount());
            return openJobChartRespDTO;
        }).collect(Collectors.toList());
    }

    private MessageResponseBody doInvoke(Message message){
        MessageResponseBody response;
        try {
            response = clusterInvoker.invoke(message);
        }catch (RpcException e){
            throw new RpcException(e.getMessage());
        }
        if (Objects.nonNull(response) && response.getStatus() != ResponseStatus.SUCCESS){
            throw new RpcException("处理失败");
        }
        return response;
    }

}
