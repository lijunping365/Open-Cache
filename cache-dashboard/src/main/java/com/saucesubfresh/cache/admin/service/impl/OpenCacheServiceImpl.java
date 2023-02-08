package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.mapper.OpenCacheLogMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheService;
import com.saucesubfresh.cache.api.dto.req.*;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheKeyRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheNameRespDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheValueRespDTO;
import com.saucesubfresh.cache.common.domain.*;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.json.JSON;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.PageResult;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.enums.ResponseStatus;
import com.saucesubfresh.rpc.core.exception.RpcException;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2023/1/31
 */
@Slf4j
@Service
public class OpenCacheServiceImpl implements OpenCacheService {

    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper openCacheAppMapper;
    private final OpenCacheLogMapper openCacheLogMapper;

    public OpenCacheServiceImpl(ClusterInvoker clusterInvoker, OpenCacheAppMapper openCacheAppMapper, OpenCacheLogMapper openCacheLogMapper) {
        this.clusterInvoker = clusterInvoker;
        this.openCacheAppMapper = openCacheAppMapper;
        this.openCacheLogMapper = openCacheLogMapper;
    }

    @Override
    public PageResult<OpenCacheNameRespDTO> cacheNames(OpenCacheNameReqDTO reqDTO) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(reqDTO.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_NAMES.getValue());
        messageBody.setCurrent(reqDTO.getCurrent());
        messageBody.setPageSize(reqDTO.getPageSize());
        message.setBody(SerializationUtils.serialize(messageBody));
        MessageResponseBody responseBody;
        try {
            responseBody = doInvoke(message);
        }catch (RpcException ex){
            throw new ServiceException(ex.getMessage());
        }

        byte[] body = responseBody.getBody();
        CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);
        if (StringUtils.isBlank(response.getData())){
            return PageResult.<OpenCacheNameRespDTO>newBuilder().build();
        }

        CacheNamePageInfo pageInfo = JSON.parse(response.getData(), CacheNamePageInfo.class);
        List<CacheNameInfo> cacheNames = pageInfo.getCacheNames();
        Integer totalSize = pageInfo.getTotalSize();
        if (CollectionUtils.isEmpty(cacheNames)){
            return PageResult.<OpenCacheNameRespDTO>newBuilder().build();
        }
        List<OpenCacheNameRespDTO> records = new ArrayList<>();
        for (CacheNameInfo cacheName : cacheNames) {
            OpenCacheNameRespDTO cacheNameRespDTO = new OpenCacheNameRespDTO();
            cacheNameRespDTO.setCacheName(cacheName.getCacheName());
            cacheNameRespDTO.setCacheKeySize(cacheName.getCacheKeySize());
            records.add(cacheNameRespDTO);
        }
        return PageResult.build(records, totalSize, reqDTO.getCurrent(), reqDTO.getPageSize());
    }

    @Override
    public PageResult<OpenCacheKeyRespDTO> cacheKeys(OpenCacheKeyReqDTO reqDTO) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(reqDTO.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_KEY_SET.getValue());
        messageBody.setCacheNames(Collections.singletonList(reqDTO.getCacheName()));
        messageBody.setCurrent(reqDTO.getCurrent());
        messageBody.setPageSize(reqDTO.getPageSize());
        message.setBody(SerializationUtils.serialize(messageBody));
        MessageResponseBody responseBody;
        try {
            responseBody = doInvoke(message);
        }catch (RpcException ex){
            throw new ServiceException(ex.getMessage());
        }

        byte[] body = responseBody.getBody();
        CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);
        if (StringUtils.isBlank(response.getData())){
            return PageResult.<OpenCacheKeyRespDTO>newBuilder().build();
        }

        CacheKeyPageInfo pageInfo = JSON.parse(response.getData(), CacheKeyPageInfo.class);

        List<String> cacheKeys = pageInfo.getCacheKeys();
        Integer totalSize = pageInfo.getTotalSize();
        if (CollectionUtils.isEmpty(cacheKeys)){
            return PageResult.<OpenCacheKeyRespDTO>newBuilder().build();
        }

        List<OpenCacheKeyRespDTO> records = new ArrayList<>();
        for (String key : cacheKeys) {
            OpenCacheKeyRespDTO cacheKeyRespDTO = new OpenCacheKeyRespDTO();
            cacheKeyRespDTO.setCacheKey(key);
            records.add(cacheKeyRespDTO);
        }
        return PageResult.build(records, totalSize, reqDTO.getCurrent(), reqDTO.getPageSize());
    }

    @Override
    public boolean preloadCache(OpenCachePreloadCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCacheNames(request.getCacheNames());
        messageBody.setCommand(CacheCommandEnum.PRELOAD.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));

        String errMsg = null;
        try {
            doInvoke(message);
        }catch (RpcException ex){
            errMsg = ex.getMessage();
        }
        //recordLog(e, response, errMsg);
        return true;
    }

    @Override
    public boolean clearCache(OpenCacheClearCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCacheNames(request.getCacheNames());
        messageBody.setCommand(CacheCommandEnum.CLEAR.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));

        String errMsg = null;
        try {
            doInvoke(message);
        }catch (RpcException ex){
            errMsg = ex.getMessage();
        }
        //recordLog(e, response, errMsg);
        return true;
    }

    @Override
    public boolean evictCache(OpenCacheEvictCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCacheNames(Collections.singletonList(request.getCacheName()));
        messageBody.setKeys(request.getKeys());
        messageBody.setCommand(CacheCommandEnum.INVALIDATE.getValue());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        message.setBody(SerializationUtils.serialize(messageBody));

        String errMsg = null;
        try {
            doInvoke(message);
        }catch (RpcException ex){
            errMsg = ex.getMessage();
        }
        //recordLog(e, response, errMsg);
        return true;
    }

    @Override
    public boolean updateCache(OpenCacheUpdateCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCacheNames(Collections.singletonList(request.getCacheName()));
        messageBody.setKeys(Collections.singletonList(request.getKey()));
        messageBody.setValue(request.getValue());
        messageBody.setCommand(CacheCommandEnum.UPDATE.getValue());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        message.setBody(SerializationUtils.serialize(messageBody));

        String errMsg = null;
        try {
            doInvoke(message);
        }catch (RpcException ex){
            errMsg = ex.getMessage();
        }
        //recordLog(e, response, errMsg);
        return true;
    }

    @Override
    public OpenCacheValueRespDTO getCache(OpenCacheGetCacheRequest request) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(request.getAppId());
        CacheMessageRequest messageBody = new CacheMessageRequest();
        messageBody.setCacheNames(Collections.singletonList(request.getCacheName()));
        messageBody.setKeys(Collections.singletonList(request.getKey()));
        messageBody.setCommand(CacheCommandEnum.GET.getValue());
        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        message.setBody(SerializationUtils.serialize(messageBody));


        MessageResponseBody responseBody;
        try {
            responseBody = doInvoke(message);
        }catch (RpcException ex){
            throw new ServiceException(ex.getMessage());
        }

        byte[] body = responseBody.getBody();
        CacheMessageResponse response = SerializationUtils.deserialize(body, CacheMessageResponse.class);

        OpenCacheValueRespDTO respDTO = new OpenCacheValueRespDTO();
        respDTO.setKey(request.getKey());
        respDTO.setValue(response.getData());
        return respDTO;
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

    private void recordLog(MessageResponseBody response, String cause){
//        OpenJobLogCreateDTO openJobLogCreateDTO = new OpenJobLogCreateDTO();
//        String requestId = Optional.ofNullable(response).map(MessageResponseBody::getRequestId).orElse("");
//        openJobLogCreateDTO.setAppId(jobDO.getAppId());
//        openJobLogCreateDTO.setJobId(jobDO.getId());
//        openJobLogCreateDTO.setStatus(StringUtils.isBlank(cause) ? CommonStatusEnum.YES.getValue() : CommonStatusEnum.NO.getValue());
//        openJobLogCreateDTO.setCause(cause);
//        openJobLogCreateDTO.setCreateTime(LocalDateTime.now());
//        JobLogEvent logEvent = new JobLogEvent(this, openJobLogCreateDTO);
//        openCacheLogMapper.publishEvent(logEvent);
    }
}
