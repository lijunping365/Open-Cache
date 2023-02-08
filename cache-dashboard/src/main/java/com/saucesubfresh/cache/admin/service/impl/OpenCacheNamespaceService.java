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
package com.saucesubfresh.cache.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.saucesubfresh.cache.admin.entity.OpenCacheAppDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheAppMapper;
import com.saucesubfresh.rpc.client.namespace.NamespaceService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijunping on 2022/8/18
 */
@Component
public class OpenCacheNamespaceService implements NamespaceService {

    private final OpenCacheAppMapper openCacheAppMapper;

    public OpenCacheNamespaceService(OpenCacheAppMapper openCacheAppMapper) {
        this.openCacheAppMapper = openCacheAppMapper;
    }

    @Override
    public List<String> loadNamespace() {
        List<OpenCacheAppDO> openCacheAppDOS = openCacheAppMapper.selectList(Wrappers.lambdaQuery());
        if (CollectionUtils.isEmpty(openCacheAppDOS)){
            return Collections.emptyList();
        }
        return openCacheAppDOS.stream().map(OpenCacheAppDO::getAppName).collect(Collectors.toList());
    }
}
