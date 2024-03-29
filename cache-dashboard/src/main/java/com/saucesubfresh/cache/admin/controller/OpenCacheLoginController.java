package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.api.dto.req.OpenCacheMobileLoginRequest;
import com.saucesubfresh.cache.api.dto.req.OpenCachePasswordLoginRequest;
import com.saucesubfresh.cache.common.exception.ControllerException;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.processor.CaptchaVerifyProcessor;
import com.saucesubfresh.starter.captcha.request.CaptchaVerifyRequest;
import com.saucesubfresh.starter.oauth.core.password.PasswordAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.core.sms.SmsMobileAuthenticationProcessor;
import com.saucesubfresh.starter.oauth.exception.AuthenticationException;
import com.saucesubfresh.starter.oauth.request.MobileLoginRequest;
import com.saucesubfresh.starter.oauth.request.PasswordLoginRequest;
import com.saucesubfresh.starter.oauth.token.AccessToken;
import com.saucesubfresh.starter.oauth.token.TokenStore;
import com.saucesubfresh.starter.security.exception.InvalidBearerTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lijunping on 2022/3/29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/login")
public class OpenCacheLoginController {

    @Autowired
    private CaptchaVerifyProcessor captchaVerifyProcessor;

    @Autowired
    private PasswordAuthenticationProcessor passwordAuthentication;

    @Autowired
    private SmsMobileAuthenticationProcessor smsMobileAuthentication;

    @Autowired
    private TokenStore tokenStore;

    /**
     * 用户名密码登录
     * @param request
     * @return
     */
    @PostMapping("/account")
    public Result<AccessToken> loginByUsername(@RequestBody @Valid OpenCachePasswordLoginRequest request){
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaVerifyProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ControllerException(e.getMessage());
        }

        PasswordLoginRequest passwordLoginRequest = new PasswordLoginRequest()
                .setUsername(request.getUsername())
                .setPassword(request.getPassword());
        try {
            final AccessToken accessToken = passwordAuthentication.authentication(passwordLoginRequest);
            return Result.succeed(accessToken);
        } catch (AuthenticationException e){
            throw new ControllerException(e.getMessage());
        }
    }

    /**
     * 手机号短信验证码登录
     * @param request
     * @return
     */
    @PostMapping("/mobile")
    public Result<AccessToken> loginByMobile(@RequestBody @Valid OpenCacheMobileLoginRequest request){
        CaptchaVerifyRequest captchaVerifyRequest = new CaptchaVerifyRequest()
                .setRequestId(request.getDeviceId())
                .setCode(request.getCaptcha());
        try {
            captchaVerifyProcessor.validate(captchaVerifyRequest);
        } catch (ValidateCodeException e){
            throw new ControllerException(e.getMessage());
        }

        MobileLoginRequest mobileLoginRequest = new MobileLoginRequest().setMobile(request.getMobile());
        try {
            final AccessToken accessToken = smsMobileAuthentication.authentication(mobileLoginRequest);
            return Result.succeed(accessToken);
        } catch (AuthenticationException e){
            throw new ControllerException(e.getMessage());
        }
    }

    @GetMapping("/refreshToken")
    public Result<AccessToken> refreshToken(@RequestParam("refreshToken") String refreshToken){
        try {
            return Result.succeed(tokenStore.refreshToken(refreshToken));
        } catch (AuthenticationException e){
            throw new InvalidBearerTokenException(e.getMessage());
        }
    }
}
