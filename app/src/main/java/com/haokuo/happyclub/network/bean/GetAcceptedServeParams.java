package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class GetAcceptedServeParams extends PageParams {
    public static final int STATUS_RECEIVED = 11;
    public static final int STATUS_SERVING = 22;
    public static final int STATUS_SERVED = 33;
    public static final int STATUS_COMPLETED = 44;
    public static final int STATUS_ABANDONED = 88;
    private int status;

    public GetAcceptedServeParams(int status) {
        this.status = status;
    }
}
