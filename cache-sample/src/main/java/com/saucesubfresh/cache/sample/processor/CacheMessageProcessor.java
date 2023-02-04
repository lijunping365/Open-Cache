package com.saucesubfresh.cache.sample.processor;

import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.server.process.MessageProcess;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.exception.CacheExecuteException;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess {

    private final CacheManager cacheManager;
    private final CacheExecutor cacheExecutor;
    private final CacheProperties cacheProperties;

    public CacheMessageProcessor(CacheManager cacheManager, CacheExecutor cacheExecutor, CacheProperties cacheProperties) {
        this.cacheManager = cacheManager;
        this.cacheExecutor = cacheExecutor;
        this.cacheProperties = cacheProperties;
    }

    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        CacheMessageBody messageBody = SerializationUtils.deserialize(body, CacheMessageBody.class);
        Result<Object> result = doProcess(messageBody);
        return SerializationUtils.serialize(result);
    }

    private Result<Object> doProcess(CacheMessageBody messageBody){
        CacheCommandEnum command = CacheCommandEnum.of(messageBody.getCommand());
        if (Objects.isNull(command)){
            return Result.failed("参数非法");
        }

        if (!command.isInner()){
            try {
                cacheExecutor.execute(convert(messageBody));
                return Result.succeed();
            }catch (CacheExecuteException e){
                log.error("缓存执行异常：{}", e.getMessage());
                return Result.failed(e.getMessage());
            }
        }

        switch (command){
            case QUERY_CACHE_NAMES:
                Collection<String> cacheNames = cacheManager.getCacheNames();
                return Result.succeed(cacheNames);
            case QUERY_CACHE_METRICS:
                List<CacheStatsInfo> cacheMetrics = getCacheMetrics(messageBody);
                return Result.succeed(cacheMetrics);
            default:
                return Result.succeed();
        }
    }

    private List<CacheStatsInfo> getCacheMetrics(CacheMessageBody messageBody){
        List<String> cacheNames = new ArrayList<>();
        String cacheName = messageBody.getCacheName();
        if (StringUtils.isNotBlank(cacheName)){
            cacheNames.add(cacheName);
        }else {
            cacheNames.addAll(cacheManager.getCacheNames());
        }

        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }

        return cacheNames.stream().map(e->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(e);
            final ClusterCache cache = cacheManager.getCache(cacheName);
            final CacheStats stats = cache.getStats();
            cacheStatsInfo.setHitCount(stats.getHitCount());
            cacheStatsInfo.setMissCount(stats.getMissCount());
            cacheStatsInfo.setRequestCount(stats.requestCount());
            cacheStatsInfo.setHitRate(stats.hitRate());
            cacheStatsInfo.setMissRate(stats.missRate());
            return cacheStatsInfo;
        }).collect(Collectors.toList());
    }

    private CacheMessage convert(CacheMessageBody messageBody){
        return CacheMessage.builder()
                .cacheName(messageBody.getCacheName())
                .command(CacheCommand.of(messageBody.getCommand()))
                .key(messageBody.getKey())
                .value(messageBody.getValue())
                .instanceId(cacheProperties.getInstanceId())
                .msgId(messageBody.getMsgId())
                .build();
    }
}
