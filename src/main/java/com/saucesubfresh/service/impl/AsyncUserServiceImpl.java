package com.saucesubfresh.service.impl;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.saucesubfresh.entity.UserEntity;
import com.saucesubfresh.service.UserService;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-05-22 20:50
 */
@Service
public class AsyncUserServiceImpl implements UserService {


    // 使用executor设置线程池
    AsyncCache<Integer, Integer> asyncCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(100).buildAsync();

    @Override
    public UserEntity loadUser(Integer id) {
        return null;
    }
}
