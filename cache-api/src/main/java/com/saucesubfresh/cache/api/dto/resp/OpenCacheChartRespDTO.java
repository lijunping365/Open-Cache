package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author lijunping on 2022/4/11
 */
@Data
public class OpenCacheChartRespDTO implements Serializable {

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 总请求总数
     */
    private Long requestCount;

    /**
     * 总命中总数
     */
    private Long hitCount;
}
