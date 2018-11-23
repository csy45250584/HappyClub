package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/22.
 */
@Data
public class GetMyActivityParams extends PageParams {
    public static final Integer STATUS_JOIN = 0;
    public static final Integer STATUS_SIGN_IN = 1;
    public static final Integer STATUS_SIGN_OUT = 2;
    private String activityName;
    private Integer joinStatus; //	标记 0:参加 1:签到 2:签退
    private String startTime;
    private String endTime;

    public GetMyActivityParams(Integer joinStatus) {
        this.joinStatus = joinStatus;
    }
}
