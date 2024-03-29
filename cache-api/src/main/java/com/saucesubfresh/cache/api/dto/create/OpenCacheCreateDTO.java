package com.saucesubfresh.cache.api.dto.create;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务表
 *
 * @author lijunping
 * @email lijunping365@gmail.com
 * @date 2021-09-06 10:10:03
 */
@Data
public class OpenCacheCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "appId 不能为空")
    private Long appId;

    @NotNull(message = "CacheName 不能为空")
    private String CacheName;

    @NotNull(message = "handlerName 不能为空")
    private String handlerName;

    @NotBlank(message = "cron 表达式不能为空")
    private String cronExpression;

    private String params;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
