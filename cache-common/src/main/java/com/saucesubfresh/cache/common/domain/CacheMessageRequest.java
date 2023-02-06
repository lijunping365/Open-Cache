package com.saucesubfresh.cache.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 李俊平
 * @Date: 2023-01-29 21:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheMessageRequest implements Serializable {
    private static final long serialVersionUID = 6990779922183880977L;

    /**
     * The cache key
     */
    private String key;
    /**
     * The cache value
     */
    private String value;
    /**
     * The message id
     */
    private String msgId;
    /**
     * The message command
     */
    private String command;
    /**
     * The page number
     */
    private Integer current = 1;
    /**
     * The page size
     */
    private Integer pageSize = 10;
    /**
     * The cache name
     */
    private List<String> cacheNames;



}
