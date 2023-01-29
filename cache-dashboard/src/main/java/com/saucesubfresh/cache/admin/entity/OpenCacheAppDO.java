package com.saucesubfresh.cache.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lijunping on 2022/6/10
 */
@Data
@TableName("open_cache_app")
public class OpenCacheAppDO implements Serializable {
    private static final long serialVersionUID = 598192183560573801L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * appName
     */
    private String appName;
    /**
     * appDesc
     */
    private String appDesc;
    /**
     * 应用创建时间
     */
    private LocalDateTime createTime;
    /**
     * 应用修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 应用创建人
     */
    private Long createUser;
    /**
     * 应用修改人
     */
    private Long updateUser;

}
