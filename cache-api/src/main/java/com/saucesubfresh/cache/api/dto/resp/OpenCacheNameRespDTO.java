package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 任务运行日志
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Data
public class OpenCacheNameRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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

}
