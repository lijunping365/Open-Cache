package com.saucesubfresh.cache.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping on 2022/6/17
 */
public enum CacheCommandEnum {

    /**
     * Invalidate local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    INVALIDATE("invalidate"),

    /**
     * Update local cache entry across all LocalCachedMap instances on map entry change. Broadcasts full map entry state (Key and Value objects) to all instances.
     */
    UPDATE("update"),

    /**
     * Clear local cache entry across all LocalCachedMap instances on map entry change.
     */
    CLEAR("clear"),

    /**
     * Preload local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    PRELOAD("preload"),

    /**
     * Preload local cache entry across all LocalCachedMap instances on map entry change. Broadcasts map entry hash (16 bytes) to all instances.
     */
    GET("get"),

    /**
     * Query cacheNames
     */
    QUERY_CACHE_NAMES("query_cache_names"),

    /**
     * Query cacheMetrics
     */
    QUERY_CACHE_METRICS("query_cache_metrics"),
    ;

    private final String value;

    CacheCommandEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CacheCommandEnum of(String value){
        for (CacheCommandEnum cacheCommand : values()) {
            if (StringUtils.equals(cacheCommand.getValue(), value)){
                return cacheCommand;
            }
        }
        return null;
    }
}
