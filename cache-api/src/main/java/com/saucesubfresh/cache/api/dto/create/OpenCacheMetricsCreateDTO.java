package com.saucesubfresh.cache.api.dto.create;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheMetricsCreateDTO implements Serializable {

    private static final long serialVersionUID = -6314523526069041299L;

    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

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

    /**
     * 任务日志创建时间
     */
    private LocalDateTime createTime;
}
