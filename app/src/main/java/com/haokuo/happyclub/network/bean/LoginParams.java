package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelPhoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class LoginParams extends TelPhoneParams {
    private String password;

    public LoginParams(String telphone, String password) {
        super(telphone);
        this.password = password;
    }
}
