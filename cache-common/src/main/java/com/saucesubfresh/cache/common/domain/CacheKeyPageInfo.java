package com.saucesubfresh.cache.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2023-02-04 18:31
 */
@Data
public class CacheKeyPageInfo implements Serializable {
    private static final long serialVersionUID = -3351168267509870896L;
    /**
     * 缓存名称
     */
    private List<String> cacheKeys;

    /**
     * 总条数
     */
    private Integer totalSize;
}
