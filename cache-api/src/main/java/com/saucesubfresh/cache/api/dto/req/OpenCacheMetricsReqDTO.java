package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.PageQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheMetricsReqDTO extends PageQuery implements Serializable {
    private static final long serialVersionUID = -2782122237609826178L;

    private Long appId;

    private String cacheName;
}
