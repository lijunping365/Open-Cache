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

    private Long id;

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
    private String key;

    /**
     * 缓存 value
     */
    private String value;

    /**
     * 命令类型
     */
    private String command;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 执行时间
     */
    private LocalDateTime createTime;

}
