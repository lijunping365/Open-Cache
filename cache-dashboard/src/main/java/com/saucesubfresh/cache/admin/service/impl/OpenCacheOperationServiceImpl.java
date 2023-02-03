package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheOperationService;
import com.saucesubfresh.cache.api.dto.req.OpenCacheClearCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheEvictCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePreloadCacheRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCacheUpdateCacheRequest;
import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheOperationServiceImpl implements OpenCacheOperationService {

    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper openCacheAppMapper;

    public OpenCacheOperationServiceImpl(ClusterInvoker clusterInvoker, OpenCacheAppMapper openCacheAppMapper) {
        this.clusterInvoker = clusterInvoker;
        this.openCacheAppMapper = openCacheAppMapper;
    }

    @Override
    public boolean preloadCache(OpenCachePreloadCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCacheName(request.getCacheName());
        messageBody.setCommand(CacheCommandEnum.PRELOAD.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        return doInvoke(message);
    }

    @Override
    public boolean clearCache(OpenCacheClearCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCacheName(request.getCacheName());
        messageBody.setCommand(CacheCommandEnum.CLEAR.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        return doInvoke(message);
    }

    @Override
    public boolean evictCache(OpenCacheEvictCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCacheName(request.getCacheName());
        messageBody.setKey(request.getKey());
        messageBody.setCommand(CacheCommandEnum.INVALIDATE.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        return doInvoke(message);
    }

    @Override
    public boolean updateCache(OpenCacheUpdateCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCacheName(request.getCacheName());
        messageBody.setKey(request.getKey());
        messageBody.setValue(request.getValue());
        messageBody.setCommand(CacheCommandEnum.UPDATE.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        return doInvoke(message);
    }

    private boolean doInvoke(Message message){
        try {
            MessageResponseBody response = clusterInvoker.invoke(message);
            Result<?> result = SerializationUtils.deserialize(response.getBody(), Result.class);
            return Objects.nonNull(result) && result.isSuccess();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
