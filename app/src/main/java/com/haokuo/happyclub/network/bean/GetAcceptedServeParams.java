package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class GetAcceptedServeParams extends PageParams {
    public static final Integer STATUS_RECEIVED = 11;
    public static final Integer STATUS_SERVING = 22;
    public static final Integer STATUS_SERVED = 33;
    public static final Integer STATUS_COMPLETED = 44;
    public static final Integer STATUS_ABANDONED = 88;
    public static final Integer STATUS_ALL = null;
    private Integer status;

    public GetAcceptedServeParams(Integer status) {
        this.status = status;
    }
}
