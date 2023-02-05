package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.service.OpenCacheInstanceService;
import com.saucesubfresh.cache.admin.service.OpenCacheMetricsService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheMetricsReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheInstanceRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheMetricsRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageRequest;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.remoting.RemotingInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.constants.CommonConstant;
import com.saucesubfresh.rpc.core.information.ServerInformation;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheMetricsServiceImpl implements OpenCacheMetricsService {

    private final RemotingInvoker remotingInvoker;
    private final OpenCacheInstanceService instanceService;

    public OpenCacheMetricsServiceImpl(RemotingInvoker remotingInvoker, OpenCacheInstanceService instanceService) {
        this.remotingInvoker = remotingInvoker;
        this.instanceService = instanceService;
    }


    @Override
    public PageResult<OpenCacheMetricsRespDTO> queryMetrics(OpenCacheMetricsReqDTO reqDTO) {
        List<OpenCacheInstanceRespDTO> cacheInstance = instanceService.getInstanceList(reqDTO.getAppId());
        if (CollectionUtils.isEmpty(cacheInstance)){
            return PageResult.<OpenCacheMetricsRespDTO>newBuilder().build();
        }

        cacheInstance.sort(Comparator.comparing(OpenCacheInstanceRespDTO::getOnlineTime).reversed());


        for (OpenCacheInstanceRespDTO instance : cacheInstance) {
            Message message = new Message();
            CacheMessageRequest messageBody = new CacheMessageRequest();
            messageBody.setCacheNames(Collections.singletonList(reqDTO.getCacheName()));
            messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_METRICS.getValue());
            message.setBody(SerializationUtils.serialize(messageBody));
            String[] serverId = instance.getServerId().split(CommonConstant.Symbol.MH);
            ServerInformation serverInformation = new ServerInformation(serverId[0], Integer.parseInt(serverId[1]));

        }
        //return PageResult.build(cacheInstance, cacheInstance.size(), instanceReqDTO.getCurrent(), instanceReqDTO.getPageSize());

        return null;
    }

    private void doInvoke(Message message, ServerInformation serverInformation){
        remotingInvoker.invoke(message, serverInformation);
    }
}
