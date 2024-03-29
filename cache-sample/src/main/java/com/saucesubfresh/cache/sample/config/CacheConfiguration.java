package com.saucesubfresh.cache.sample.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.saucesubfresh.starter.cache.executor.CacheExecutor;
import com.saucesubfresh.starter.cache.factory.ConfigFactory;
import com.saucesubfresh.starter.cache.handler.CacheListenerErrorHandler;
import com.saucesubfresh.starter.cache.handler.CacheProducerErrorHandler;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.manager.RedisCaffeineCacheManager;
import com.saucesubfresh.starter.cache.manager.RedissonCaffeineCacheManager;
import com.saucesubfresh.starter.cache.message.*;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;

/**
 * @author: 李俊平
 * @Date: 2022-03-19 10:24
 */
@Configuration
public class CacheConfiguration {

    @Bean
    public NamingService namingService() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.USERNAME, "nacos");
        properties.put(PropertyKeyConst.PASSWORD, "nacos");
        properties.put(PropertyKeyConst.SERVER_ADDR, "127.0.0.1:8848");
        return NacosFactory.createNamingService(properties);
    }

//    @Bean
//    public CacheManager cacheManager(CacheProperties properties,
//                                     ConfigFactory configFactory,
//                                     RedisTemplate<String, Object> redisTemplate){
//        return new RedisCaffeineCacheManager(properties, configFactory, redisTemplate);
//    }

    @Bean
    public CacheMessageProducer cacheMessageProducer(CacheProperties properties,
                                                     CacheProducerErrorHandler errorHandler,
                                                     RedisTemplate<String, Object> redisTemplate){
        return new RedisCacheMessageProducer(properties, errorHandler, redisTemplate);
    }

    @Bean
    public CacheMessageListener cacheMessageListener(CacheExecutor cacheExecutor,
                                                     CacheProperties properties,
                                                     CacheListenerErrorHandler errorHandler,
                                                     RedisTemplate<String, Object> redisTemplate){
        return new RedisCacheMessageListener(cacheExecutor, properties, errorHandler, redisTemplate);
    }

//    @Bean
//    public CacheMessageProducer cacheMessageProducer(CacheProperties properties,
//                                                     KafkaTemplate<String, Object> kafkaTemplate){
//        return new KafkaCacheMessageProducer(properties, kafkaTemplate);
//    }
//
//    @Bean
//    public CacheMessageListener cacheMessageListener(CacheExecutor cacheExecutor,
//                                                     CacheProperties properties,
//                                                     KafkaTemplate<String, Object> kafkaTemplate){
//        return new KafkaCacheMessageListener(cacheExecutor, properties, kafkaTemplate);
//    }
}
