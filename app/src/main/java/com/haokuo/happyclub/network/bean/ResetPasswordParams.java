package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelphoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class ResetPasswordParams extends TelphoneParams {
    private String code;
    private String newPwd;
    private String verificationPwd;

    public ResetPasswordParams(String telphone, String code, String newPwd, String verificationPwd) {
        super(telphone);
        this.code = code;
        this.newPwd = newPwd;
        this.verificationPwd = verificationPwd;
    }
}
