package com.saucesubfresh.cache.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
     * The message id
     */
    private String msgId;
    /**
     * The cache name
     */
    private String cacheName;
    /**
     * The message command
     */
    private String command;
    /**
     * The cache key
     */
    private Object key;
    /**
     * The cache value
     */
    private Object value;


}
