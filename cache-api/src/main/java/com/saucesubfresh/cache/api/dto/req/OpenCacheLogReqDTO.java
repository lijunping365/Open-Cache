package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.DateTimePageQuery;
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
public class OpenCacheLogReqDTO extends DateTimePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long CacheId;

    private Integer status;

    private String cause;

}
