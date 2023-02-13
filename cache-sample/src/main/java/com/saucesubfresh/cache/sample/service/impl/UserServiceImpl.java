package com.saucesubfresh.cache.sample.service.impl;

import com.saucesubfresh.cache.sample.domain.UserDO;
import com.saucesubfresh.cache.sample.mapper.UserMapper;
import com.saucesubfresh.cache.sample.service.UserService;
import com.saucesubfresh.starter.cache.annotation.OpenCacheEvict;
import com.saucesubfresh.starter.cache.annotation.OpenCachePut;
import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import com.saucesubfresh.starter.cache.processor.CacheProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lijunping on 2022/6/17
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final CacheProcessor cacheProcessor;


    public UserServiceImpl(CacheProcessor cacheProcessor, UserMapper userMapper) {
        this.cacheProcessor = cacheProcessor;
        this.userMapper = userMapper;
    }

    /**
     * 此处演示在更新数据时为了保持 mysql 和 缓存数据的一致性所采取的方案抉择：
     *
     * 1. 先更新 MySQL，再更新 Redis，这种方案极不推荐使用
     *
     * 1. 先更新 MySQL，再删除 Redis，推荐使用该方案
     */
    //@OpenCachePut // 不推荐使用
    @OpenCacheEvict(cacheName = "test", key = "#userDO.id") // 推荐使用该方案
    @Override
    public void updateUser(UserDO userDO) {
        userMapper.update(userDO);
    }

    @OpenCacheable(cacheName = "test", key = "#id")
    @Override
    public UserDO loadUserById(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test2", key = "#id")
    @Override
    public UserDO loadUserById2(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test3", key = "#id")
    @Override
    public UserDO loadUserById3(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test4", key = "#id")
    @Override
    public UserDO loadUserById4(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test5", key = "#id")
    @Override
    public UserDO loadUserById5(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test6", key = "#id")
    @Override
    public UserDO loadUserById6(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test7", key = "#id")
    @Override
    public UserDO loadUserById7(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test8", key = "#id")
    @Override
    public UserDO loadUserById8(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test9", key = "#id")
    @Override
    public UserDO loadUserById9(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test10", key = "#id")
    @Override
    public UserDO loadUserById10(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test11", key = "#id")
    @Override
    public UserDO loadUserById11(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test12", key = "#id")
    @Override
    public UserDO loadUserById12(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test13", key = "#id")
    @Override
    public UserDO loadUserById13(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test14", key = "#id")
    @Override
    public UserDO loadUserById14(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }


    @OpenCacheable(cacheName = "test15", key = "#id")
    @Override
    public UserDO loadUserById15(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test16", key = "#id")
    @Override
    public UserDO loadUserById16(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test17", key = "#id")
    @Override
    public UserDO loadUserById17(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test18", key = "#id")
    @Override
    public UserDO loadUserById18(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test19", key = "#id")
    @Override
    public UserDO loadUserById19(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test20", key = "#id")
    @Override
    public UserDO loadUserById20(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test21", key = "#id")
    @Override
    public UserDO loadUserById21(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test22", key = "#id")
    @Override
    public UserDO loadUserById22(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test23", key = "#id")
    @Override
    public UserDO loadUserById23(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test24", key = "#id")
    @Override
    public UserDO loadUserById24(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test25", key = "#id")
    @Override
    public UserDO loadUserById25(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test26", key = "#id")
    @Override
    public UserDO loadUserById26(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test27", key = "#id")
    @Override
    public UserDO loadUserById27(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test28", key = "#id")
    @Override
    public UserDO loadUserById28(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test29", key = "#id")
    @Override
    public UserDO loadUserById29(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test30", key = "#id")
    @Override
    public UserDO loadUserById30(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test31", key = "#id")
    @Override
    public UserDO loadUserById31(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test32", key = "#id")
    @Override
    public UserDO loadUserById32(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test33", key = "#id")
    @Override
    public UserDO loadUserById33(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

    @OpenCacheable(cacheName = "test34", key = "#id")
    @Override
    public UserDO loadUserById34(Long id) {
        log.info("没走缓存");
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setName("lijunping & pengguifang");
        return userDO;
    }

}
