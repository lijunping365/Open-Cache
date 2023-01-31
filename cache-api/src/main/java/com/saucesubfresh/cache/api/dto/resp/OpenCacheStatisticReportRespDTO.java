package com.saucesubfresh.cache.api.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/4/11
 */
@Data
public class OpenCacheStatisticReportRespDTO implements Serializable {

    private LocalDateTime date;

    private String name;

    private Integer value;
}