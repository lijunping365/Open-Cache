package com.saucesubfresh.service;

import com.saucesubfresh.entity.UserEntity;

/**
 * @author: 李俊平
 * @Date: 2022-05-21 09:24
 */
public interface UserService {

    UserEntity loadUser(Integer id);
}
