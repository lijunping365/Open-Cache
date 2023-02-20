package com.saucesubfresh.cache.sample.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * redis自动配置
 */
@Configuration
@AutoConfigureBefore({RedisTemplate.class, RedisAutoConfiguration.class})
public class RedisConfig {

    /**
     * json序列化
     */
    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        JsonJacksonCodec jsonJacksonCodec = new JsonJacksonCodec();
        ObjectMapper mapper = jsonJacksonCodec.getObjectMapper();
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        //StringRedisTemplate的构造方法中默认设置了stringSerializer
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //set key serializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        //set default serializer
        template.setDefaultSerializer(jackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties properties) {
        Config config = new Config();
        final SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setDatabase(properties.getDatabase());
        if (StringUtils.isNotBlank(properties.getPassword())) {
            singleServerConfig.setPassword(properties.getPassword());
        }
        config.setCodec(new TypedJsonJacksonCodec(Object.class));

        return Redisson.create(config);
    }

//    @Bean
//    public RedisMessageListenerContainer cacheMessageListenerContainer(CacheProperties cacheProperties,
//                                                                       CacheMessageListener cacheMessageListener,
//                                                                       LettuceConnectionFactory connectionFactory) {
//
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
//        MessageListener messageListener = (MessageListener) cacheMessageListener;
//        redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(cacheProperties.getNamespace()));
//        return redisMessageListenerContainer;
//    }
}