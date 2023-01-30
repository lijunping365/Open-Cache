package com.saucesubfresh.cache.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 缓存命中率统计实体类
 * @author lijunping on 2022/6/17
 */
@Data
public class CacheStatsInfo implements Serializable {
    private static final long serialVersionUID = -8492906785137983152L;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 缓存实例 id
     */
    private String instanceId;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 总请求总数
     */
    private long requestCount;

    /**
     * 总命中总数
     */
    private long hitCount;

    /**
     * 总未命中总数
     */
    private long missCount;

    /**
     * 命中率
     */
    private double hitRate;

    /**
     * 未命中率
     */
    private double missRate;
}
