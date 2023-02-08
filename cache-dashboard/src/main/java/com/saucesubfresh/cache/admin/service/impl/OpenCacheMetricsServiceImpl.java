package com.saucesubfresh.cache.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saucesubfresh.cache.admin.convert.OpenCacheMetricsConvert;
import com.saucesubfresh.cache.admin.entity.OpenCacheMetricsDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheMetricsMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.admin.service.OpenCacheMetricsService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageResponse;
import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.json.JSON;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.remoting.RemotingInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.constants.CommonConstant;
import com.saucesubfresh.rpc.core.enums.ResponseStatus;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.core.information.ServerInformation;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheMetricsServiceImpl implements OpenCacheMetricsService {

    private final RemotingInvoker remotingInvoker;
    private final OpenCacheInstanceService instanceService;
    private final OpenCacheMetricsMapper metricsMapper;

    public OpenCacheMetricsServiceImpl(RemotingInvoker remotingInvoker,
                                       OpenCacheInstanceService instanceService,
                                       OpenCacheMetricsMapper metricsMapper) {
        this.remotingInvoker = remotingInvoker;
        this.instanceService = instanceService;
        this.metricsMapper = metricsMapper;
    }

    @Override
    public PageResult<OpenCacheMetricsRespDTO> selectPage(OpenCacheMetricsReqDTO reqDTO) {
        if (!reqDTO.isCurrent()){
            Page<OpenCacheMetricsDO> page = metricsMapper.queryPage(reqDTO);
            IPage<OpenCacheMetricsRespDTO> convert = page.convert(OpenCacheMetricsConvert.INSTANCE::convert);
            return PageResult.build(convert);
        }

        List<OpenCacheInstanceRespDTO> cacheInstance = instanceService.getInstanceList(reqDTO.getAppId());
        if (CollectionUtils.isEmpty(cacheInstance)){
            return PageResult.<OpenCacheMetricsRespDTO>newBuilder().build();
        }

        cacheInstance.sort(Comparator.comparing(OpenCacheInstanceRespDTO::getOnlineTime).reversed());

        int totalSize = cacheInstance.size();
        Integer pageNum = reqDTO.getCurrent();
        Integer pageSize = reqDTO.getPageSize();
        String cacheName = reqDTO.getCacheName();

        //进行分页处理
        if ((pageNum -1) * pageSize >= totalSize){
            return PageResult.<OpenCacheMetricsRespDTO>newBuilder().build();
        }
        int endIndex = Math.min(totalSize, pageNum * pageSize);

        List<String> serverIds = new ArrayList<>();
        for (int i = (pageNum -1) * pageSize; i < endIndex; i++) {
            serverIds.add(cacheInstance.get(i).getServerId());
        }

        List<OpenCacheMetricsRespDTO> metricsRespDTOS = new ArrayList<>();
        for (String serverId : serverIds) {
            Message message = new Message();
            CacheMessageRequest messageBody = new CacheMessageRequest();
            messageBody.setCacheNames(Collections.singletonList(cacheName));
            messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_METRICS.getValue());
            message.setBody(SerializationUtils.serialize(messageBody));
            String[] serverIdArray = serverId.split(CommonConstant.Symbol.MH);
            ServerInformation serverInformation = new ServerInformation(serverIdArray[0], Integer.parseInt(serverIdArray[1]));

            MessageResponseBody responseBody;
            try {
                responseBody = doInvoke(message, serverInformation);
            }catch (RpcException ex){
                throw new ServiceException(ex.getMessage());
            }

            byte[] body = responseBody.getBody();
            CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);
            if (StringUtils.isBlank(response.getData())){
                continue;
            }

            CacheStatsInfo statsInfo = JSON.parse(response.getData(), CacheStatsInfo.class);
            metricsRespDTOS.add(convert(statsInfo, reqDTO.getAppId(), cacheName));
        }

        return PageResult.build(metricsRespDTOS, totalSize, reqDTO.getCurrent(), reqDTO.getPageSize());
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

    private OpenCacheMetricsRespDTO convert(CacheStatsInfo statsInfo, Long appId, String cacheName){
        OpenCacheMetricsRespDTO metricsRespDTO = new OpenCacheMetricsRespDTO();
        metricsRespDTO.setAppId(appId);
        metricsRespDTO.setCacheName(cacheName);
        metricsRespDTO.setInstanceId(statsInfo.getInstanceId());
        metricsRespDTO.setRequestCount(statsInfo.getRequestCount());
        metricsRespDTO.setHitCount(statsInfo.getHitCount());
        metricsRespDTO.setMissCount(statsInfo.getMissCount());
        metricsRespDTO.setHitRate(BigDecimal.valueOf(statsInfo.getHitRate()));
        metricsRespDTO.setMissRate(BigDecimal.valueOf(statsInfo.getMissRate()));
        metricsRespDTO.setCreateTime(LocalDateTime.now());
        return metricsRespDTO;
    }
}
