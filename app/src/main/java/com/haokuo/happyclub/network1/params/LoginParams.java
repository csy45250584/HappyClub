package com.haokuo.happyclub.network1.params;

import lombok.Data;

/**
 * Created by zjf on 2018/9/6.
 */
@Data
public class LoginParams {
    private String telphone;
    private String password;

    public LoginParams(String telphone, String password) {
        this.telphone = telphone;
        this.password = password;
    }
}
