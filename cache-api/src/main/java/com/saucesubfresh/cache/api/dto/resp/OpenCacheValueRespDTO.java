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
public class OpenCacheValueRespDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 缓存 key
     */
    private String key;
    /**
     * 缓存 value
     */
    private String value;


}
