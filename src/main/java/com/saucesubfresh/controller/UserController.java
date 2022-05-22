package com.saucesubfresh.controller;

import com.saucesubfresh.entity.UserEntity;
import com.saucesubfresh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 李俊平
 * @Date: 2022-05-21 09:22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/loadUser")
    public UserEntity loadUser(@RequestParam Integer id){
        return userService.loadUser(id);
    }
}
