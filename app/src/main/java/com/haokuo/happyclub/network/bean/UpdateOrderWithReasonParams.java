package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/30.
 */
@Data
public class UpdateOrderWithReasonParams extends UserIdTokenParams {
    public static final int OPERATION_REFUND_CLUB = 11; //会所服务
    public static final int OPERATION_REFUND = 78;
    public static final int OPERATION_REFUND_PAYED = 7;

    private long orderId;
    private String reason;
    private int opStatus;

    public UpdateOrderWithReasonParams(long orderId, String reason, int opStatus) {
        this.orderId = orderId;
        this.reason = reason;
        this.opStatus = opStatus;
    }
}
