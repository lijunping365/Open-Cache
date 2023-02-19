package com.saucesubfresh.cache.sample.component.config;

import com.saucesubfresh.starter.cache.factory.AbstractConfigFactory;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 *     加载配置文件的默认实现，默认加载 yaml 文件
 * </p>
 *
 * @author lijunping
 */
@Slf4j
@Component
public class CustomConfigFactory extends AbstractConfigFactory implements ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    public CustomConfigFactory(CacheProperties properties) {
        super(properties);
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

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
