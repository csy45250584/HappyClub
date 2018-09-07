package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelphoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class LoginParams extends TelphoneParams {
    private String password;

    public LoginParams(String telphone, String password) {
        super(telphone);
        this.password = password;
    }
}
