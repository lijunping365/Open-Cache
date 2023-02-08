package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.DateTimePageQuery;
import com.saucesubfresh.cache.common.vo.PageQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheMetricsReqDTO extends DateTimePageQuery implements Serializable {
    private static final long serialVersionUID = -2782122237609826178L;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 是否只查询当前时间的指标数据
     */
    private boolean current = true;
}
