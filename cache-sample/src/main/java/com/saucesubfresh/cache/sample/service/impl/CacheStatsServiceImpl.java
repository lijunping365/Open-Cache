package com.saucesubfresh.cache.sample.service.impl;

import com.saucesubfresh.cache.common.domain.CacheStatsInfo;
import com.saucesubfresh.cache.sample.service.CacheStatsService;
import com.saucesubfresh.starter.cache.core.ClusterCache;
import com.saucesubfresh.starter.cache.manager.CacheManager;
import com.saucesubfresh.starter.cache.stats.CacheStats;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 李俊平
 * @Date: 2022-06-08 23:39
 */
@Service
public class CacheStatsServiceImpl implements CacheStatsService {

    private final CacheManager cacheManager;

    public CacheStatsServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public List<CacheStatsInfo> getCacheStatsInfo() {
        final Collection<String> cacheNames = cacheManager.getCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return Collections.emptyList();
        }

        return cacheNames.stream().map(cacheName->{
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(cacheName);
            final ClusterCache cache = cacheManager.getCache(cacheName);
            final CacheStats stats = cache.getStats();
            cacheStatsInfo.setHitCount(stats.getHitCount());
            cacheStatsInfo.setMissCount(stats.getMissCount());
            cacheStatsInfo.setRequestCount(stats.requestCount());
            cacheStatsInfo.setHitRate(stats.hitRate());
            cacheStatsInfo.setMissRate(stats.missRate());
            return cacheStatsInfo;
        }).collect(Collectors.toList());
    }
}
