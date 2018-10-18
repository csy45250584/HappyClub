package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/15.
 */
@Data
public class GetOrderListParams extends PageParams {
    private Integer orderType;
    private Integer status;
    private String startTime;
    private String endTime;

    public GetOrderListParams(Integer orderType, Integer status, String startTime, String endTime) {
        this.orderType = orderType;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
