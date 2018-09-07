package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelphoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class LoginByTelParams extends TelphoneParams {
    private String code;

    public LoginByTelParams(String telphone, String code) {
        super(telphone);
        this.code = code;
    }
}
