package com.saucesubfresh.cache.sample.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/17
 */
@Data
public class UserDO implements Serializable {

    private Long id;

    private String name;


}
