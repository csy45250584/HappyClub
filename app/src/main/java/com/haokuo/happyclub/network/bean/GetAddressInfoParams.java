package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/7.
 */
@Data
public class GetAddressInfoParams extends UserIdTokenParams {
    private long id;

    public GetAddressInfoParams(long id) {
        super();
        this.id = id;
    }
}
