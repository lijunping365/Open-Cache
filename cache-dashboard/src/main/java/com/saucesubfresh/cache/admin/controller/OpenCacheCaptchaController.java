package com.saucesubfresh.cache.admin.controller;

import com.saucesubfresh.cache.api.dto.req.OpenCacheCaptchaRequest;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheCaptchaRespDTO;
import com.saucesubfresh.cache.common.exception.ControllerException;
import com.saucesubfresh.cache.common.vo.Result;
import com.saucesubfresh.starter.captcha.core.image.ImageCodeGenerator;
import com.saucesubfresh.starter.captcha.core.image.ImageValidateCode;
import com.saucesubfresh.starter.captcha.core.sms.SmsCodeGenerator;
import com.saucesubfresh.starter.captcha.core.sms.ValidateCode;
import com.saucesubfresh.starter.captcha.exception.ValidateCodeException;
import com.saucesubfresh.starter.captcha.request.CaptchaGenerateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lijunping on 2022/3/29
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/captcha")
public class OpenCacheCaptchaController {

    private final ImageCodeGenerator imageCodeGenerator;
    private final SmsCodeGenerator smsCodeGenerator;

    public OpenCacheCaptchaController(ImageCodeGenerator imageCodeGenerator, SmsCodeGenerator smsCodeGenerator) {
        this.imageCodeGenerator = imageCodeGenerator;
        this.smsCodeGenerator = smsCodeGenerator;
    }

    @PostMapping("/create/image")
    public Result<OpenCacheCaptchaRespDTO> createImageCode(@RequestBody @Valid OpenCacheCaptchaRequest request) {
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        try {
            ImageValidateCode imageValidateCode = imageCodeGenerator.create(captchaGenerateRequest);
            return Result.succeed(convert(imageValidateCode));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
    }

    @PostMapping("/create/sms")
    public Result<OpenCacheCaptchaRespDTO> createSmsCode(@RequestBody @Valid OpenCacheCaptchaRequest request) {
        OpenCacheCaptchaRespDTO openCacheCaptchaRespDTO = new OpenCacheCaptchaRespDTO();
        CaptchaGenerateRequest captchaGenerateRequest = new CaptchaGenerateRequest();
        captchaGenerateRequest.setRequestId(request.getDeviceId());
        try {
            ValidateCode validateCode = smsCodeGenerator.create(captchaGenerateRequest);
            openCacheCaptchaRespDTO.setSuccess(true);
            log.info("向手机号: {}发送短信验证码: {}", request.getMobile(), validateCode.getCode());
        } catch (ValidateCodeException e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }
        return Result.succeed(openCacheCaptchaRespDTO);
    }

    private OpenCacheCaptchaRespDTO convert(ImageValidateCode imageValidateCode) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageValidateCode.getImage(), "JPEG", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64ImgCode = Base64Utils.encodeToString(bytes);
        OpenCacheCaptchaRespDTO openCacheCaptchaRespDTO = new OpenCacheCaptchaRespDTO();
        openCacheCaptchaRespDTO.setImageCode(base64ImgCode);
        openCacheCaptchaRespDTO.setSuccess(true);
        return openCacheCaptchaRespDTO;
    }

}
