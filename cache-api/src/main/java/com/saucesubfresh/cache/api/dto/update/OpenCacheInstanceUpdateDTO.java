package com.saucesubfresh.cache.api.dto.update;

import com.saucesubfresh.rpc.core.enums.Status;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: 李俊平
 * @Date: 2022-02-26 15:03
 */
@Data
public class OpenCacheInstanceUpdateDTO implements Serializable {
    private static final long serialVersionUID = 4199379444084998224L;

    /**
     * 客户端地址：端口
     */
    private String clientId;
    /**
     * 上线时间
     */
    private LocalDateTime onlineTime;
    /**
     * 在线状态
     */
    private Status status;
    /**
     * 权重
     */
    private int weight = 1;
}
