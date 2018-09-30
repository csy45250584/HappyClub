package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/30.
 */
@Data
public class GetBindTelCodeParams extends UserIdTokenParams {
    private String telphone;

    public GetBindTelCodeParams(String telphone) {
        this.telphone = telphone;
    }
}
