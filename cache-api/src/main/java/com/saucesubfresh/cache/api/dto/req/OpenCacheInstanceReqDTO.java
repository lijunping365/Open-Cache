package com.saucesubfresh.cache.api.dto.req;

import com.saucesubfresh.cache.common.vo.DateTimePageQuery;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:03
 */
@Data
public class OpenCacheInstanceReqDTO extends DateTimePageQuery implements Serializable {
    private static final long serialVersionUID = 4199379444084998224L;

    /**
     * 应用 id
     */
    private Long appId;
    /**
     * 服务端 【地址：端口】
     */
    private String serverId;
    /**
     * 上线时间
     */
    private LocalDateTime onlineTime;
    /**
     * 在线状态
     */
    private String status;
    /**
     * 权重
     */
    private int weight = 1;
}
