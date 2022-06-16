package com.saucesubfresh.cache;

import com.saucesubfresh.rpc.server.annotation.EnableOpenRpcServer;
import com.saucesubfresh.starter.security.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:03
 */
@EnableSecurity
@EnableOpenRpcServer
@SpringBootApplication
public class CacheAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheAdminApplication.class, args);
    }
}
