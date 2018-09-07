package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class UpdatePasswordParams extends UserIdTokenParams {
    private String telphone;
    private String password;
    private String newPWD;

    public UpdatePasswordParams(String telphone, String password, String newPWD) {
        super();
        this.telphone = telphone;
        this.password = password;
        this.newPWD = newPWD;
    }
}
