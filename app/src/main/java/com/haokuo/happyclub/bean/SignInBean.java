package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/10/25.
 */
@Data
public class SignInBean {
    private Integer credit;
    private long id;
    private Integer state;
    private String signinDate;
    private long userId;
}
