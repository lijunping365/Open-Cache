package com.saucesubfresh.cache.sample.controller;

import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.cache.sample.domain.UserDO;
import com.saucesubfresh.cache.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijunping on 2022/6/17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/load")
    public Result<UserDO> loadUser(@RequestParam Long id){
        final UserDO userDO = userService.loadUserById(id);
        return Result.succeed(userDO);
    }
}
