package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelphoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class RegisterParams extends TelphoneParams {
    private String code;
    private String password;

    public RegisterParams(String telphone, String code, String password) {
        super(telphone);
        this.code = code;
        this.password = password;
    }
}
