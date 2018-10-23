package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/12.
 */
@Data
public class UpdateOrderParams extends UserIdTokenParams {
    public static final int OPERATION_PAY = 2;
    public static final int OPERATION_COMPLETE = 6;
    public static final int OPERATION_CANCEL = 8;

    private long orderId;
    private Integer payType; //仅针对下单的字段 0:积分,1:支付宝,2:微信,3:银联,4:积分+其他,
    private int opStatus; //付款:2  完成 : 6

    public UpdateOrderParams(long orderId, Integer payType, int opStatus) {
        this.orderId = orderId;
        this.payType = payType;
        this.opStatus = opStatus;
    }
}
