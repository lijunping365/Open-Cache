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

    @NotBlank(message = "应用id不能为空")
    private Long appId;

    @NotBlank(message = "缓存名称不能为空")
    private String cacheName;

}
