package com.saucesubfresh.cache.event;

import com.saucesubfresh.cache.api.dto.create.OpenCacheLogCreateDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author lijunping on 2022/2/28
 */
@Getter
public class CacheLogEvent extends ApplicationEvent {
    private final OpenCacheLogCreateDTO cacheLogCreateDTO;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public CacheLogEvent(Object source, OpenCacheLogCreateDTO cacheLogCreateDTO) {
        super(source);
        this.cacheLogCreateDTO = cacheLogCreateDTO;
    }
}
