package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/9.
 */
@Data
public class GetServiceProviderDetailParams extends UserIdTokenParams {
    private Long businessId;

    public GetServiceProviderDetailParams(Long businessId) {
        this.businessId = businessId;
    }
}
