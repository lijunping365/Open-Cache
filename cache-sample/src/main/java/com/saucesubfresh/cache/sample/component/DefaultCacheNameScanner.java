package com.saucesubfresh.cache.sample.component;

import com.saucesubfresh.starter.cache.annotation.OpenCacheable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: 李俊平
 * @Date: 2023-02-19 22:46
 */
@Slf4j
@Component
public class DefaultCacheNameScanner implements CacheNameScanner, ApplicationContextAware, InitializingBean {

    protected final Set<String> cacheNames = new HashSet<>();

    private ApplicationContext applicationContext;

    @Override
    public Set<String> loadCacheNames() {
        return cacheNames;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.scanMethodOpenCacheable();
    }

    private void scanMethodOpenCacheable(){
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

            for (Map.Entry<Method, OpenCacheable> methodOpenCacheableEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodOpenCacheableEntry.getKey();
                OpenCacheable annotation = methodOpenCacheableEntry.getValue();
                buildJobHandler(annotation, bean, executeMethod);
            }
        }
    }

    private void buildJobHandler(OpenCacheable openCacheable, Object bean, Method executeMethod){
        if (openCacheable == null) {
            return;
        }

        String name = openCacheable.value();
        Class<?> clazz = bean.getClass();
        String methodName = executeMethod.getName();
        if (StringUtils.isBlank(name)) {
            throw new RuntimeException("The value of annotation OpenCacheable is required , for[" + clazz + "#" + methodName + "] .");
        }

        cacheNames.add(name);
    }
}
