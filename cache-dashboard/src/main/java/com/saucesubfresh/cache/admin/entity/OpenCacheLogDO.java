package com.saucesubfresh.cache.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 缓存操作日志
 * 
 * @author lijunping
 */
@Data
@TableName("open_cache_log")
public class OpenCacheLogDO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 应用 id
	 */
	private Long appId;
	/**
	 * 缓存实例 id
	 */
	private String instanceId;
	/**
	 * 缓存名称
	 */
	private String cacheName;
	/**
	 * 缓存 key
	 */
	private String cacheKey;
	/**
	 * 缓存 value
	 */
	private String cacheValue;
	/**
	 * 命令类型
	 */
	private String command;
	/**
	 * 任务执行状态（1 成功，0 失败）
	 */
	private Integer status;
	/**
	 * 任务失败原因
	 */
	private String cause;
	/**
	 * 任务日志创建时间
	 */
	private LocalDateTime createTime;

}
