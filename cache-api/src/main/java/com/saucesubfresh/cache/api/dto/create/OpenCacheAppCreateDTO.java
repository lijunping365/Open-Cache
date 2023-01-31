package com.saucesubfresh.cache.api.dto.create;

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
public class OpenCacheAppCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String appName;

    private String appDesc;

    /**
     * 应用创建时间
     */
    private LocalDateTime createTime;
    /**
     * 应用修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 应用创建人
     */
    private Long createUser;
    /**
     * 应用修改人
     */
    private Long updateUser;

}
