package com.saucesubfresh.cache.admin.service;

import java.util.List;

/**
 * @author lijunping on 2023/2/3
 */
public interface OpenCacheNameService {

    List<String> getCacheNames(Long appId);
}
