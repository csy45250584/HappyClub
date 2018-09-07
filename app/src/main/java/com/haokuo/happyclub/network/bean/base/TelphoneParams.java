package com.haokuo.happyclub.network.bean.base;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class TelphoneParams {
    private String telphone;

    public TelphoneParams(String telphone) {
        this.telphone = telphone;
    }
}
