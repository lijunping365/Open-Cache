package com.saucesubfresh.cache.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lijunping on 2022/4/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenCacheStatisticRespDTO implements Serializable {
    /**
     * 应用数量
     */
    private Integer appNum;
    /**
     * cacheName 总数量
     */
    private Integer cacheNameCount;
    /**
     * 节点数量
     */
    private Integer nodeCount;
    /**
     * 缓存名称
     */
    private String cacheName;
    /**
     * 总请求总数
     */
    private Long requestCount;
    /**
     * 总命中总数
     */
    private Long hitCount;
    /**
     * 运行时长
     */
    private String liveTime;
    /**
     * cpu使用信息
     */
    private String cpuInfo;
    /**
     * 内存使用信息
     */
    private String memoryInfo;
    /**
     * 磁盘使用信息
     */
    private String diskInfo;

}
