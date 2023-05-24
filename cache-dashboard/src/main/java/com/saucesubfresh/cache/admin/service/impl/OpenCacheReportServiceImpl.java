package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.admin.entity.OpenCacheReportDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheMetricsMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheReportMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.admin.service.OpenCacheReportService;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheChartRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenTopKRespDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/4/11
 */
@Slf4j
@Service
public class OpenCacheReportServiceImpl implements OpenCacheReportService {

    private final InstanceStore instanceStore;
    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper cacheAppMapper;
    private final OpenCacheMetricsMapper metricsMapper;
    private final OpenCacheReportMapper cacheReportMapper;
    private final OpenCacheInstanceService openCacheInstanceService;

    public OpenCacheReportServiceImpl(InstanceStore instanceStore,
                                      ClusterInvoker clusterInvoker,
                                      OpenCacheAppMapper cacheAppMapper,
                                      OpenCacheMetricsMapper metricsMapper,
                                      OpenCacheReportMapper cacheReportMapper,
                                      OpenCacheInstanceService openCacheInstanceService) {
        this.instanceStore = instanceStore;
        this.clusterInvoker = clusterInvoker;
        this.cacheAppMapper = cacheAppMapper;
        this.metricsMapper = metricsMapper;
        this.cacheReportMapper = cacheReportMapper;
        this.openCacheInstanceService = openCacheInstanceService;
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
            List<OpenCacheMetricsDO> cacheMetricsByCacheName = metricsMapper.groupByCacheName(
                            cacheMetricByAppId.getAppId(),
                            startTime,
                            endTime
                    );
            for (OpenCacheMetricsDO cacheMetricByCacheName : cacheMetricsByCacheName) {
                List<OpenCacheMetricsDO> cacheMetricsByInstanceId = metricsMapper.groupByInstanceId(
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
    public OpenCacheStatisticRespDTO getStatistic() {
        List<OpenCacheAppDO> openCacheAppDOS = cacheAppMapper.queryList(null);
        if (CollectionUtils.isEmpty(openCacheAppDOS)){
            return null;
        }

        int nodeCount = 0;
        int cacheNameCount = 0;
        for (OpenCacheAppDO openCacheAppDO : openCacheAppDOS) {
            try {
                OpenCacheStatisticRespDTO appStatistic = getAppStatistic(openCacheAppDO.getId());
                nodeCount += appStatistic.getNodeCount();
                cacheNameCount += appStatistic.getCacheNameCount();
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }

        return OpenCacheStatisticRespDTO.builder()
                .appNum(openCacheAppDOS.size())
                .cacheNameCount(cacheNameCount)
                .nodeCount(nodeCount)
                .build();
    }

    @Override
    public OpenCacheStatisticRespDTO getAppStatistic(Long appId) {
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
    public OpenCacheStatisticRespDTO getCacheNameStatistic(Long appId, String cacheName) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTimeUtil.getDayStart(now);
        LocalDateTime endTime = LocalDateTimeUtil.getDayEnd(now);
        List<OpenCacheMetricsDO> metricsDOS = metricsMapper.queryList(appId, cacheName, null, startTime, endTime);
        if (CollectionUtils.isEmpty(metricsDOS)){
            return null;
        }

        Long totalRequestCount = metricsDOS.stream().map(OpenCacheMetricsDO::getRequestCount).reduce(Long::sum).orElse(0L);
        Long totalHitCount = metricsDOS.stream().map(OpenCacheMetricsDO::getHitCount).reduce(Long::sum).orElse(0L);
        return OpenCacheStatisticRespDTO.builder()
                .cacheName(cacheName)
                .requestCount(totalRequestCount)
                .hitCount(totalHitCount)
                .build();
    }

    @Override
    public OpenCacheStatisticRespDTO getInstanceStatistic(Long appId, String serverId) {
        OpenCacheInstanceRespDTO instance = openCacheInstanceService.getInstanceById(appId, serverId);
        if (Objects.isNull(instance)){
            return null;
        }

        return OpenCacheStatisticRespDTO.builder()
                .liveTime(instance.getLiveTime())
                .cpuInfo(instance.getCpuInfo())
                .memoryInfo(instance.getMemoryInfo())
                .diskInfo(instance.getDiskInfo())
                .build();
    }

    @Override
    public List<OpenCacheChartRespDTO> getChart(Long appId, String cacheName, String instanceId, Integer count) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTimeUtil.getDayStart(startTime.plusDays(count));
        List<OpenCacheReportDO> openCacheReportDOS = cacheReportMapper.queryList(appId, cacheName, instanceId, startTime, endTime);
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

    @Override
    public List<OpenTopKRespDTO> getCacheNameTopK(Long appId, String instanceId, Integer count, Integer top) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTimeUtil.getDayStart(startTime.plusDays(count));
        List<OpenCacheReportDO> openCacheReportDOS = cacheReportMapper.queryList(appId, null, instanceId, startTime, endTime);
        if (CollectionUtils.isEmpty(openCacheReportDOS)){
            return Collections.emptyList();
        }

        Map<String, List<OpenCacheReportDO>> groupMap = openCacheReportDOS.stream().collect(Collectors.groupingBy(
                OpenCacheReportDO::getCacheName
        ));

        return getTopK(groupMap, top);
    }

    @Override
    public List<OpenTopKRespDTO> getInstanceTopK(Long appId, String cacheName, Integer count, Integer top) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTimeUtil.getDayStart(startTime.plusDays(count));
        List<OpenCacheReportDO> openCacheReportDOS = cacheReportMapper.queryList(appId, cacheName, null, startTime, endTime);
        if (CollectionUtils.isEmpty(openCacheReportDOS)){
            return Collections.emptyList();
        }

        Map<String, List<OpenCacheReportDO>> groupMap = openCacheReportDOS.stream().collect(Collectors.groupingBy(
                OpenCacheReportDO::getInstanceId
        ));

        return getTopK(groupMap, top);
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

    public List<OpenTopKRespDTO> getTopK(Map<String, List<OpenCacheReportDO>> groupMap, Integer top) {
        List<OpenTopKRespDTO> topKRespDTOS = new ArrayList<>();
        for (Map.Entry<String, List<OpenCacheReportDO>> entry : groupMap.entrySet()) {
            List<OpenCacheReportDO> value = entry.getValue();
            Long totalRequestCount = value.stream().map(OpenCacheReportDO::getTotalRequestCount).reduce(Long::sum).orElse(0L);
            Long totalHitCount = value.stream().map(OpenCacheReportDO::getTotalHitCount).reduce(Long::sum).orElse(0L);
            OpenTopKRespDTO openTopKRespDTO = new OpenTopKRespDTO();
            openTopKRespDTO.setKey(entry.getKey());
            openTopKRespDTO.setRequestCount(totalRequestCount);
            openTopKRespDTO.setHitCount(totalHitCount);
            topKRespDTOS.add(openTopKRespDTO);
        };

        if (topKRespDTOS.size() > top){
            topKRespDTOS = topKRespDTOS.subList(0, top);
        }

        topKRespDTOS.sort((u1, u2) -> u2.getRequestCount().compareTo(u1.getRequestCount()));

        return topKRespDTOS;
    }

}
