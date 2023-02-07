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
//        for (int i = 1; i < 10000000; i++) {
//            userService.loadUserById(id);
//        }
        final UserDO userDO = userService.loadUserById(id);
        userService.loadUserById2(id);
        userService.loadUserById3(id);
        userService.loadUserById4(id);
        userService.loadUserById5(id);
        userService.loadUserById6(id);
        userService.loadUserById7(id);
        userService.loadUserById8(id);
        userService.loadUserById9(id);
        userService.loadUserById10(id);
        userService.loadUserById11(id);
        userService.loadUserById12(id);
        userService.loadUserById13(id);
        userService.loadUserById14(id);
        userService.loadUserById15(id);
        userService.loadUserById16(id);
        userService.loadUserById17(id);
        userService.loadUserById18(id);
        userService.loadUserById19(id);
        userService.loadUserById20(id);
        userService.loadUserById21(id);
        userService.loadUserById22(id);
        userService.loadUserById23(id);
        userService.loadUserById24(id);
        userService.loadUserById25(id);
        userService.loadUserById26(id);
        userService.loadUserById27(id);
        userService.loadUserById28(id);
        userService.loadUserById29(id);
        userService.loadUserById30(id);
        userService.loadUserById31(id);
        userService.loadUserById32(id);
        userService.loadUserById33(id);
        userService.loadUserById34(id);

        return Result.succeed(userDO);
    }


}
