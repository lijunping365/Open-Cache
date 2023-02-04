package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheService;
import com.saucesubfresh.cache.api.dto.req.*;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheNameRespDTO;
import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2023/1/31
 */
@Service
public class OpenCacheServiceImpl implements OpenCacheService {

    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper openCacheAppMapper;

    public OpenCacheServiceImpl(ClusterInvoker clusterInvoker, OpenCacheAppMapper openCacheAppMapper) {
        this.clusterInvoker = clusterInvoker;
        this.openCacheAppMapper = openCacheAppMapper;
    }

    @Override
    public PageResult<OpenCacheNameRespDTO> cacheNames(OpenCacheNameReqDTO reqDTO) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(reqDTO.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_NAMES.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        Result<?> result = doInvoke(message);
        if (Objects.isNull(result) || Objects.isNull(result.getData())){
            return PageResult.<OpenCacheNameRespDTO>newBuilder().build();
        }

        List<String> cacheNames = (List<String>)result.getData();
        List<OpenCacheNameRespDTO> records = new ArrayList<>();
        for (String cacheName : cacheNames) {
            OpenCacheNameRespDTO cacheNameRespDTO = new OpenCacheNameRespDTO();
            cacheNameRespDTO.setCacheName(cacheName);
            records.add(cacheNameRespDTO);
        }

        return PageResult.build(records, records.size(), reqDTO.getCurrent(), reqDTO.getPageSize());
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
        Result<?> result = doInvoke(message);
        return Objects.nonNull(result) && result.isSuccess();
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
        Result<?> result = doInvoke(message);
        return Objects.nonNull(result) && result.isSuccess();
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
        Result<?> result = doInvoke(message);
        return Objects.nonNull(result) && result.isSuccess();
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
        Result<?> result = doInvoke(message);
        return Objects.nonNull(result) && result.isSuccess();
    }

    private Result<?> doInvoke(Message message){
        try {
            MessageResponseBody response = clusterInvoker.invoke(message);
            return SerializationUtils.deserialize(response.getBody(), Result.class);
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
