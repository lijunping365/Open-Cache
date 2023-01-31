package com.saucesubfresh.cache.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheMetricsReqDTO implements Serializable {
    private static final long serialVersionUID = -2782122237609826178L;

    @NotBlank(message = "应用名称不能为空")
    private String namespace;

    @NotBlank(message = "服务id不能为空")
    private String serverId;

    private String cacheName;
}