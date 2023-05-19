package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheMetricsMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheReportMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheStatisticRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageResponse;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.time.LocalDateTimeUtil;
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

import java.time.LocalDateTime;
import java.util.*;
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

    private final OpenCacheReportMapper cacheReportMapper;

    public OpenCacheReportServiceImpl(InstanceStore instanceStore,
                                      ClusterInvoker clusterInvoker,
                                      OpenCacheAppMapper cacheAppMapper,
                                      OpenCacheMetricsMapper metricsMapper,
                                      OpenCacheReportMapper cacheReportMapper) {
        this.instanceStore = instanceStore;
        this.clusterInvoker = clusterInvoker;
        this.cacheAppMapper = cacheAppMapper;
        this.metricsMapper = metricsMapper;
        this.cacheReportMapper = cacheReportMapper;
    }

    @Override
    public void generateReport(LocalDateTime now) {
        LocalDateTime startTime = LocalDateTimeUtil.getDayStart(now);
        LocalDateTime endTime = LocalDateTimeUtil.getDayEnd(now);

        List<OpenCacheMetricsDO> cacheMetricsByAppId = metricsMapper.groupByAppId(startTime, endTime);
        if (CollectionUtils.isEmpty(cacheMetricsByAppId)){
            return;
        }

        for (OpenCacheMetricsDO cacheMetricByAppId : cacheMetricsByAppId) {
            List<OpenCacheMetricsDO> cacheMetricsByCacheName = metricsMapper
                    .groupByCacheName(
                            cacheMetricByAppId.getAppId(),
                            startTime,
                            endTime
                    );
            for (OpenCacheMetricsDO cacheMetricByCacheName : cacheMetricsByCacheName) {
                List<OpenCacheMetricsDO> cacheMetricsByInstanceId = metricsMapper
                        .groupByInstanceId(
                                cacheMetricByAppId.getAppId(),
                                cacheMetricByCacheName.getCacheName(),
                                startTime,
                                endTime
                        );
                for (OpenCacheMetricsDO cacheMetricByInstanceId : cacheMetricsByInstanceId) {
                    List<OpenCacheMetricsDO> cacheMetricsDOS = metricsMapper.queryList(
                            cacheMetricByAppId.getAppId(),
                            cacheMetricByCacheName.getCacheName(),
                            cacheMetricByInstanceId.getInstanceId(),
                            startTime,
                            endTime
                    );

                    Long totalRequestCount = cacheMetricsDOS.stream()
                            .map(OpenCacheMetricsDO::getRequestCount)
                            .reduce(Long::sum).orElse(0L);

                    Long totalHitCount = cacheMetricsDOS.stream()
                            .map(OpenCacheMetricsDO::getHitCount)
                            .reduce(Long::sum).orElse(0L);

                    OpenCacheReportDO openCacheReportDO = new OpenCacheReportDO();
                    openCacheReportDO.setAppId(cacheMetricByAppId.getAppId());
                    openCacheReportDO.setCacheName(cacheMetricByCacheName.getCacheName());
                    openCacheReportDO.setInstanceId(cacheMetricByInstanceId.getInstanceId());
                    openCacheReportDO.setTotalRequestCount(totalRequestCount);
                    openCacheReportDO.setTotalHitCount(totalHitCount);
                    openCacheReportDO.setCreateTime(endTime);
                    cacheReportMapper.insert(openCacheReportDO);
                }
            }
        }
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
    public List<OpenCacheChartRespDTO> getChart(Long appId, String cacheName, String instanceId, Integer count) {
        List<OpenCacheReportDO> openCacheReportDOS = cacheReportMapper.queryList(appId, cacheName, instanceId, count);
        if (CollectionUtils.isEmpty(openCacheReportDOS)){
            return Collections.emptyList();
        }

        Map<LocalDateTime, List<OpenCacheReportDO>> groupByDateMap = openCacheReportDOS.stream().collect(Collectors.groupingBy(
                OpenCacheReportDO::getCreateTime
        ));

        List<OpenCacheChartRespDTO> chartRespDTOS = new ArrayList<>();
        groupByDateMap.forEach((k, v) ->{
            Long totalRequestCount = v.stream().map(OpenCacheReportDO::getTotalRequestCount).reduce(Long::sum).orElse(0L);
            Long totalHitCount = v.stream().map(OpenCacheReportDO::getTotalHitCount).reduce(Long::sum).orElse(0L);
            OpenCacheChartRespDTO openCacheChartRespDTO = new OpenCacheChartRespDTO();
            openCacheChartRespDTO.setDate(k.toLocalDate());
            openCacheChartRespDTO.setRequestCount(totalRequestCount);
            openCacheChartRespDTO.setHitCount(totalHitCount);
            chartRespDTOS.add(openCacheChartRespDTO);
        });

        return chartRespDTOS;
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
