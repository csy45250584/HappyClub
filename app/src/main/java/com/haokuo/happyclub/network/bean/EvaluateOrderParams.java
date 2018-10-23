package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/30.
 */
@Data
public class EvaluateOrderParams extends UserIdTokenParams {
    public static final int OPERATION_EVALUATE = 9;
    public static final int OPERATION_REPLY = 10;
    private long orderId;
    private String evaluation;
    private String src;
    private int star;
    private int opStatus;

    public EvaluateOrderParams(long orderId, String evaluation, String src, int star, int opStatus) {
        this.orderId = orderId;
        this.evaluation = evaluation;
        this.src = src;
        this.star = star;
        this.opStatus = opStatus;
    }
}
