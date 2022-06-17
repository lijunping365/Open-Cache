package com.saucesubfresh.cache.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lijunping on 2022/6/17
 */
@Getter
@AllArgsConstructor
public enum CommandType {

    PRELOAD(0),

    CLEAR(1),

    EVICT(2)

    ;

    private final int value;
}
