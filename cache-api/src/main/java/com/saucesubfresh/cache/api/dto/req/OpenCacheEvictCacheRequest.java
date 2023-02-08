package com.saucesubfresh.cache.api.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author lijunping on 2023/1/31
 */
@Data
public class OpenCacheEvictCacheRequest implements Serializable {
    private static final long serialVersionUID = -3473064349983754433L;

    @NotNull(message = "应用id不能为空")
    private Long appId;

    @NotBlank(message = "缓存名称不能为空")
    private String cacheName;

    @NotEmpty(message = "缓存key不能为空")
    private List<String> keys;


}
