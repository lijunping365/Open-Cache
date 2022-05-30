package com.saucesubfresh;

//import com.saucesubfresh.starter.cache.annotation.EnableCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author: 李俊平
 * @Date: 2022-05-21 09:16
 */
//@EnableCache
@EnableCaching
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}
