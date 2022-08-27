package com.saucesubfresh.cache;

import com.saucesubfresh.rpc.client.annotation.EnableOpenRpcClient;
import com.saucesubfresh.starter.security.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: 李俊平
 * @Date: 2022-05-29 14:03
 */
@EnableSecurity
@EnableOpenRpcClient
@SpringBootApplication
public class CacheDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheDashboardApplication.class, args);
    }
}
