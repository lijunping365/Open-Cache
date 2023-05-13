package com.saucesubfresh.cache.api.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lijunping on 2022/4/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenCacheStatisticRespDTO implements Serializable {

    /**
     * 任务总数量
     */
    private Integer cacheNameCount;
    /**
     * 任务运行数量
     */
    private Integer nodeCount;

}
