package com.saucesubfresh.cache.sample.processor;

import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.common.enums.CacheCommandEnum;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.server.process.MessageProcess;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.exception.CacheExecuteException;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess {

    private final CacheManager cacheManager;
    private final CacheExecutor cacheExecutor;

    public CacheMessageProcessor(CacheManager cacheManager, CacheExecutor cacheExecutor) {
        this.cacheManager = cacheManager;
        this.cacheExecutor = cacheExecutor;
    }

    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        CacheMessageBody messageBody = SerializationUtils.deserialize(body, CacheMessageBody.class);
        CacheCommandEnum command = CacheCommandEnum.of(messageBody.getCommand());
        if (Objects.isNull(command)){
            return "非法参数异常".getBytes(StandardCharsets.UTF_8);
        }
        if (command.isInner()){
            switch (command){
                case QUERY_CACHE_NAMES:
                    return null;
                case QUERY_CACHE_METRICS:
                    return null;
            }
        }
        try {
            cacheExecutor.execute(convert(messageBody));
        }catch (CacheExecuteException e){
            log.error("缓存执行异常：{}", e.getMessage());
            return null;
        }


        return null;
    }

    private void getCacheNames(){
        final Collection<String> cacheNames = cacheManager.getCacheNames();
    }

    private List<CacheStatsInfo> getCacheMetrics(){
        Collection<String> cacheNames = cacheManager.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }

        return cacheNames.stream().map(cacheName->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(cacheName);
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
                .instanceId(messageBody.getInstanceId())
                .msgId(messageBody.getMsgId())
                .build();
    }
}
