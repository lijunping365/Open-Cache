package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/4/11
 */
@Data
public class OpenTopKRespDTO implements Serializable {

    /**
     * key
     */
    private String key;

    /**
     * 总请求总数
     */
    private Long requestCount;

    /**
     * 总命中总数
     */
    private Long hitCount;
}
