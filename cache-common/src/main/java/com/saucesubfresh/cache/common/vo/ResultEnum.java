package com.saucesubfresh.cache.common.vo;

/**
 * 结果枚举
 *
 * @author lijunping
 */
public enum ResultEnum {

  SUCCESS(200, "成功"), UNAUTHORIZED(401, "认证失败"),

  BUSINESS_EXCEPTION("业务异常"), USERNAME_OR_PASSWORD_ERROR("用户名或密码错误"), FORBIDDEN("权限不足，无法访问"),

  ;

  private final Integer code;

  private final String msg;

  ResultEnum(String msg) {
    this.code = 1000;
    this.msg = msg;
  }

  ResultEnum(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

}
