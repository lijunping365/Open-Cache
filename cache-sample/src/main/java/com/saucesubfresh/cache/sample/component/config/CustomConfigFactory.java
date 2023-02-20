package com.saucesubfresh.cache.sample.component.config;

import com.saucesubfresh.cache.sample.component.CacheNameScanner;
import com.saucesubfresh.starter.cache.factory.AbstractConfigFactory;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     加载配置文件的默认实现，默认加载 yaml 文件
 * </p>
 *
 * @author lijunping
 */
@Slf4j
@Component
public class CustomConfigFactory extends AbstractConfigFactory implements ResourceLoaderAware, InitializingBean {

    private ResourceLoader resourceLoader;
    private final CacheNameScanner cacheNameScanner;

    public CustomConfigFactory(CacheProperties properties,
                               CacheNameScanner cacheNameScanner) {
        super(properties);
        this.cacheNameScanner = cacheNameScanner;
    }

    @Override
    protected Map<String, ? extends CacheConfig> loadConfig() {
        final String configLocation = properties.getConfigLocation();
        if (StringUtils.isBlank(configLocation)) {
            return Collections.emptyMap();
        }

        try {
            Resource resource = resourceLoader.getResource(configLocation);
            return CacheConfig.fromYAML(resource.getInputStream());
        } catch (IOException e) {
            log.warn("加载配置文件：{}，异常 {}", properties.getConfigLocation(), e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void afterPropertiesSet() throws Exception {
        Map<String, ? extends CacheConfig> config = this.loadConfig();
        if (!CollectionUtils.isEmpty(config)) {
            configMap.putAll(config);
        }
        Set<String> cacheNames = cacheNameScanner.loadCacheNames();
        if (CollectionUtils.isEmpty(cacheNames)){
            return;
        }

        cacheNames.forEach(cacheName-> configMap.putIfAbsent(cacheName, createDefault()));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
