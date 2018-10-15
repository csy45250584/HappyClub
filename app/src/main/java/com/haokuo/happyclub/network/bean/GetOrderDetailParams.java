package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/12.
 */
@Data
public class GetOrderDetailParams extends UserIdTokenParams {
   public static final int Type_SERVICE = 0;
   public static final int TYPE_CANTEEN = 1;
   public static final int TYPE_MALL = 2;
    private long orderId;
    private int orderType; //服务:0 食堂:1 积分:2

    public GetOrderDetailParams(long orderId, int orderType) {
        this.orderId = orderId;
        this.orderType = orderType;
    }
}
