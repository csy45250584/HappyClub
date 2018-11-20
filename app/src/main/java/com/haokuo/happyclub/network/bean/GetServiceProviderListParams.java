package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class GetServiceProviderListParams extends PageParams {
    private Long areaId;
    private String businessName;

    public GetServiceProviderListParams(Long areaId) {
        this.areaId = areaId;
    }

    public GetServiceProviderListParams(String businessName) {
        this.businessName = businessName;
    }
}
