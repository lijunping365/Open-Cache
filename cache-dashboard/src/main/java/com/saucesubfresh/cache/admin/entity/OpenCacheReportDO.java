package com.saucesubfresh.cache.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缓存操作日志
 * 
 * @author lijunping
 */
@Data
@TableName("open_cache_report")
public class OpenCacheReportDO implements Serializable {
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
	 * 总请求总数
	 */
	private Long totalRequestCount;

	/**
	 * 总命中总数
	 */
	private Long totalHitCount;

	/**
	 * 任务日志创建时间
	 */
	private LocalDateTime createTime;

}
