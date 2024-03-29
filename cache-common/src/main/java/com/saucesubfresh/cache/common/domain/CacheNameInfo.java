package com.saucesubfresh.cache.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 李俊平
 * @Date: 2023-02-04 18:31
 */
@Data
public class CacheNameInfo implements Serializable {
    private static final long serialVersionUID = 4131518130544129556L;

    /**
     * 键值条目的存活时间，以秒为单位。
     */
    private Long ttl;
    /**
     * 缓存容量（缓存数量最大值）
     */
    private Integer maxSize;
    /**
     * 缓存名称
     */
    private String cacheName;
    /**
     * 缓存 key size
     */
    private Long cacheKeySize;
}
