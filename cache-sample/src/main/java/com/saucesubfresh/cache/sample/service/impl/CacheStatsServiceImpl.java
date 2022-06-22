package com.saucesubfresh.cache.sample.service.impl;

import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.sample.service.CacheStatsService;
import com.saucesubfresh.starter.cache.manager.CacheManager;

import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
public class CacheStatsServiceImpl implements CacheStatsService {

    private final CacheManager cacheManager;

    public CacheStatsServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheStatsInfo> getCacheStatsInfo() {
        return null;
    }
}
