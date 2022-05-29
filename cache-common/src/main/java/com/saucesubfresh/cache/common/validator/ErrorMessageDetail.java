package com.saucesubfresh.cache.common.validator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saucesubfresh.cache.common.json.JSON;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class ErrorMessageDetail {
  @JsonProperty("错误字段")
  private String propertyPath;
  @JsonProperty("错误信息")
  private String message;
  @JsonProperty("错误值")
  private Object invalidValue;

  @Override
  public String toString() {
    return JSON.toJSON(this);
  }
}
