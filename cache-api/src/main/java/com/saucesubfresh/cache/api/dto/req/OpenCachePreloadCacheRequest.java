package com.saucesubfresh.cache.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCachePreloadCacheRequest implements Serializable {
    private static final long serialVersionUID = -3473064349983754433L;

    @NotBlank(message = "应用名称不能为空")
    private String namespace;

    @NotBlank(message = "缓存名称不能为空")
    private String cacheName;

    @NotBlank(message = "服务id不能为空")
    private String serverId;

}
