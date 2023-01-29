package com.saucesubfresh.cache.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lijunping on 2022/6/17
 */
@Getter
@AllArgsConstructor
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
    ;

    private final String value;

    public static CacheCommandEnum of(String value){
        for (CacheCommandEnum cacheCommand : values()) {
            if (StringUtils.equals(cacheCommand.getValue(), value)){
                return cacheCommand;
            }
        }
        return null;
    }
}
