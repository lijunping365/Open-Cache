package com.saucesubfresh.cache.sample.processor;

import com.saucesubfresh.cache.common.domain.CacheMessageBody;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.rpc.server.process.MessageProcess;
import com.saucesubfresh.starter.cache.exception.CacheException;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.message.CacheCommand;
import com.saucesubfresh.starter.cache.message.CacheMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess {

    private final CacheExecutor cacheExecutor;

    public CacheMessageProcessor(CacheExecutor cacheExecutor) {
        this.cacheExecutor = cacheExecutor;
    }

    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        CacheMessageBody messageBody = SerializationUtils.deserialize(body, CacheMessageBody.class);
        try {
            cacheExecutor.execute(convert(messageBody));
        }catch (CacheException e){
            log.error("缓存执行异常：{}", e.getCacheMessage());
        }


        return null;
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
