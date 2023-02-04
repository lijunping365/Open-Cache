package com.saucesubfresh.cache.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author: 李俊平
 * @Date: 2023-02-04 15:05
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CacheMessageResponse implements Serializable {
    private static final long serialVersionUID = 1985669064159084510L;

    private Object data;
}
