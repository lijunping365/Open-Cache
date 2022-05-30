package com.saucesubfresh.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.saucesubfresh.entity.UserEntity;
import com.saucesubfresh.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author: 李俊平
 * @Date: 2022-05-21 09:25
 */
@Service
public class UserServiceImpl implements UserService {

    LoadingCache<Integer, UserEntity> cache = Caffeine.newBuilder()
            //.expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1) // 缓存最大数量
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .recordStats() // 记录命中率
            .build(this::getInDB);// 同步加载数据,在get不到数据时最终会调用build构造时提供的CacheLoader对象中的load函数，如果返回值则将其插入缓存中，并且返回，这是一种同步的操作
            // 实际应用：在我司项目中，会利用这个同步机制，也就是在CacheLoader对象中的load函数中，当从Caffeine缓存中取不到数据的时候则从数据库中读取数据，通过这个机制和数据库结合使用


    @PostConstruct
    public void init(){
        for (int i = 1; i <10; i++) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(i);
            userEntity.setUsername("zhangsna");
            userEntity.setMobile("129765654");
            cache.put(i, userEntity);
        }
    }

    @Override
    @Cacheable(cacheNames = "user", key = "#root.methodName")
    @CacheEvict(cacheNames = "user", key = "#root.methodName")
    public UserEntity loadUser(Integer id) {

        // 查找一个缓存元素， 没有查找到的时候返回null
        UserEntity obj = cache.getIfPresent(id);

        cache.get(id, (key)->{
            return cache.getIfPresent(key);
        });


        CacheStats stats = cache.stats();

        System.out.println(stats.hitCount());
        System.out.println(stats.hitRate());

        // 移除数据，让数据失效
        cache.invalidate(id);

        return obj;
    }

    /**
     * 模拟从数据库中读取key
     *
     * @param key
     * @return
     */
    private UserEntity getInDB(int key) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(key);
        userEntity.setUsername("zhangsna");
        userEntity.setMobile("129765654");
        return userEntity;
    }

}
