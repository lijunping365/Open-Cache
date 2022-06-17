package com.saucesubfresh.cache.sample.service;

import com.saucesubfresh.cache.sample.domain.UserDO;

/**
 * @author lijunping on 2022/6/17
 */
public interface UserService {

    UserDO loadUserById(Long id);
}
