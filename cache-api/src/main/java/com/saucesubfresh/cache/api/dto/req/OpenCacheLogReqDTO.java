package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.DateTimePageQuery;
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
public class OpenCacheLogReqDTO extends DateTimePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存 key
     */
    private String cacheKey;

    /**
     * 命令类型
     */
    private String command;

    /**
     * 执行状态
     */
    private Integer status;

}
