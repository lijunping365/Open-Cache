package com.saucesubfresh.cache.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping on 2022/6/17
 */
public enum CacheCommandEnum {

    QUERY_CACHE_NAMES(true, "query_cache_names"), QUERY_CACHE_METRICS(true, "query_cache_names"),

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
    ;

    private final String value;

    private final boolean inner;

    CacheCommandEnum(String value) {
        this.inner = false;
        this.value = value;
    }

    CacheCommandEnum(boolean inner, String value) {
        this.inner = inner;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isInner() {
        return inner;
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
