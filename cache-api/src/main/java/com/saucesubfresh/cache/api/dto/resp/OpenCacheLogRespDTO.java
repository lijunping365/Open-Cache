package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Data
public class OpenCacheLogRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 缓存 key
     */
    private String cacheKey;
    /**
     * 缓存 value
     */
    private String cacheValue;
    /**
     * 命令类型
     */
    private String command;
    /**
     * 任务执行状态（1 成功，0 失败）
     */
    private Integer status;
    /**
     * 任务失败原因
     */
    private String cause;
    /**
     * 任务日志创建时间
     */
    private LocalDateTime createTime;

}
