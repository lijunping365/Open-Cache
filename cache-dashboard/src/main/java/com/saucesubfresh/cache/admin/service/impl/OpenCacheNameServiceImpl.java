package com.saucesubfresh.cache.admin.service.impl;

import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheNameService;
import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.exception.ServiceException;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.rpc.client.cluster.ClusterInvoker;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.core.transport.MessageResponseBody;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author lijunping on 2023/2/3
 */
@Service
public class OpenCacheNameServiceImpl implements OpenCacheNameService {

    private final ClusterInvoker clusterInvoker;
    private final OpenCacheAppMapper openCacheAppMapper;

    public OpenCacheNameServiceImpl(ClusterInvoker clusterInvoker, OpenCacheAppMapper openCacheAppMapper) {
        this.clusterInvoker = clusterInvoker;
        this.openCacheAppMapper = openCacheAppMapper;
    }

    @Override
    public List<String> getCacheNames(Long appId) {
        OpenCacheAppDO openCacheAppDO = openCacheAppMapper.selectById(appId);
        if (Objects.isNull(openCacheAppDO)){
            return Collections.emptyList();
        }

        Message message = new Message();
        message.setNamespace(openCacheAppDO.getAppName());
        CacheMessageBody messageBody = new CacheMessageBody();
        messageBody.setCommand(CacheCommandEnum.QUERY_CACHE_NAMES.getValue());
        message.setBody(SerializationUtils.serialize(messageBody));
        try {
            MessageResponseBody response = clusterInvoker.invoke(message);
            Result<?> result = SerializationUtils.deserialize(response.getBody(), Result.class);
            return Objects.isNull(result.getData()) ? null : (List<String>) result.getData();
        }catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
