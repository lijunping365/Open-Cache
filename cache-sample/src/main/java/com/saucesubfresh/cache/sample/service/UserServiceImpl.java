package com.saucesubfresh.cache.sample.service;

import com.saucesubfresh.cache.sample.domain.UserDO;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import org.springframework.stereotype.Service;

/**
 * @author lijunping on 2022/6/17
 */
@Service
public class UserServiceImpl implements UserService{

    @OpenCacheable(cacheName = "test", key = "#id")
    @Override
    public UserDO loadUserById(Long id) {
        return null;
    }
}
