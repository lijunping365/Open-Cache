package com.saucesubfresh.cache.sample.component.metrics;

import com.saucesubfresh.starter.cache.metrics.CacheMetrics;
import com.saucesubfresh.starter.cache.metrics.CacheMetricsPusher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时上报缓存情况
 *
 * @author lijunping on 2022/6/24
 */
@Component
public class ICacheMetricsPusher implements CacheMetricsPusher {

    @Override
    public void pushCacheMetrics(List<CacheMetrics> list) {

    }
}
