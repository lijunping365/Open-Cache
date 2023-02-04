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
     * 缓存名称
     */
    private String cacheName;

    /**
     * 本地缓存 key size
     */
    private Integer localCacheKeySize;

    /**
     * 远程缓存 key size
     */
    private Integer remoteCacheKeySize;

}
