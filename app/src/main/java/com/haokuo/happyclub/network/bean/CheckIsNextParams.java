package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.TelPhoneParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class CheckIsNextParams extends TelPhoneParams {
    private String code;

    public CheckIsNextParams(String telphone, String code) {
        super(telphone);
        this.code = code;
    }
}
