package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.PageQuery;
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
public class OpenCacheKeyReqDTO extends PageQuery implements Serializable {
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
     * cacheKey 模糊查询匹配规则，例如 *loadUserById*，loadUserById*，*loadUserById
     */
    private String keyPattern;

}
