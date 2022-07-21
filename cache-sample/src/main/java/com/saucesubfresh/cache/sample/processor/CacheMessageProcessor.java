package com.saucesubfresh.cache.sample.processor;

import com.saucesubfresh.cache.common.domain.MessageBody;
import com.saucesubfresh.cache.common.serialize.SerializationUtils;
import com.saucesubfresh.rpc.client.process.MessageProcess;
import com.saucesubfresh.rpc.core.Message;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lijunping on 2022/2/25
 */
@Slf4j
@Component
public class CacheMessageProcessor implements MessageProcess{

    private final CacheExecutor cacheExecutor;

    public CacheMessageProcessor(CacheExecutor cacheExecutor) {
        this.cacheExecutor = cacheExecutor;
    }


    @Override
    public byte[] process(Message message) {
        final byte[] body = message.getBody();
        MessageBody messageBody = SerializationUtils.deserialize(body, MessageBody.class);
        //cacheExecutor.execute(messageBody);

        return null;
    }
}
