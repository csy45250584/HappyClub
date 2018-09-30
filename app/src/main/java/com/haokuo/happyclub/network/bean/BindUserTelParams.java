package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/30.
 */
@Data
public class BindUserTelParams extends UserIdTokenParams {
    private String telphone;
    private String code;

    public BindUserTelParams(String telphone, String code) {
        this.telphone = telphone;
        this.code = code;
    }
}
