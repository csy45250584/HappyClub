package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/14.
 */
@Data
public class GetPointsTransferFlagParams extends UserIdTokenParams {
    private String telphone;

    public GetPointsTransferFlagParams(String telphone) {
        this.telphone = telphone;
    }
}
