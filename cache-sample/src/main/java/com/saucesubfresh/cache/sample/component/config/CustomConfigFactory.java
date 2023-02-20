package com.saucesubfresh.cache.sample.component.config;

import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import com.saucesubfresh.starter.cache.factory.AbstractConfigFactory;
import com.saucesubfresh.starter.cache.factory.CacheConfig;
import com.saucesubfresh.starter.cache.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 *     加载配置文件的默认实现，默认加载 yaml 文件
 * </p>
 *
 * @author lijunping
 */
@Slf4j
@Component
public class CustomConfigFactory extends AbstractConfigFactory implements ResourceLoaderAware, ApplicationContextAware, InitializingBean {

    private ResourceLoader resourceLoader;
    private ApplicationContext applicationContext;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        Map<String, ? extends CacheConfig> config = this.loadConfig();
        if (!CollectionUtils.isEmpty(config)) {
            configMap.putAll(config);
        }
        List<String> cacheNames = scanMethodOpenCacheable();
        if (CollectionUtils.isEmpty(cacheNames)){
            return;
        }

        cacheNames.forEach(cacheName-> configMap.putIfAbsent(cacheName, createDefault()));
    }

    private List<String> scanMethodOpenCacheable(){
        List<String> cacheNames = new ArrayList<>();
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            Map<Method, OpenCacheable> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<OpenCacheable>() {
                            @Override
                            public OpenCacheable inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, OpenCacheable.class);
                            }
                        });
            } catch (Throwable ex) {
                log.error("OpenCacheable resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods == null || annotatedMethods.isEmpty()) {
                continue;
            }

            List<String> cacheNameList = new ArrayList<>();
            for (Map.Entry<Method, OpenCacheable> methodOpenCacheableEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodOpenCacheableEntry.getKey();
                OpenCacheable openCacheable = methodOpenCacheableEntry.getValue();
                if (openCacheable == null) {
                    continue;
                }

                String cacheName = openCacheable.value();
                if (StringUtils.isBlank(cacheName)) {
                    throw new RuntimeException("The value of annotation OpenCacheable is required , for[" + bean.getClass() + "#" + executeMethod.getName() + "] .");
                }
                cacheNameList.add(cacheName);
            }
            cacheNames.addAll(cacheNameList);
        }
        return cacheNames;
    }

}
