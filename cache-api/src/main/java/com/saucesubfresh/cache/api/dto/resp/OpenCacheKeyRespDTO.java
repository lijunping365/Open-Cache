package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 缓存 key
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 */
@Data
public class OpenCacheKeyRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 缓存名称
     */
    private String cacheKey;

}
