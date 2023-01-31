package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheMetricsRespDTO implements Serializable {

    private static final long serialVersionUID = -6314523526069041299L;
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
