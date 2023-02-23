/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.cache.event;

import com.saucesubfresh.cache.admin.service.OpenCacheLogService;
import com.saucesubfresh.cache.api.dto.create.OpenCacheLogCreateDTO;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author lijunping on 2022/2/28
 */
@Component
public class CacheLogEventListener implements ApplicationListener<CacheLogEvent> {

    private final OpenCacheLogService openCacheLogService;

    public CacheLogEventListener(OpenCacheLogService openCacheLogService) {
        this.openCacheLogService = openCacheLogService;
    }

    @Override
    public void onApplicationEvent(CacheLogEvent event) {
        final OpenCacheLogCreateDTO cacheLogCreateDTO = event.getCacheLogCreateDTO();
        openCacheLogService.save(cacheLogCreateDTO);
    }
}
