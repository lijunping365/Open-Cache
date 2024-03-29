package com.saucesubfresh.cache;

import com.saucesubfresh.rpc.server.annotation.EnableOpenRpcServer;
import com.saucesubfresh.starter.cache.annotation.EnableOpenCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:07
 */
@EnableOpenCache
@EnableOpenRpcServer
@SpringBootApplication
public class CacheSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheSampleApplication.class, args);
    }
}
