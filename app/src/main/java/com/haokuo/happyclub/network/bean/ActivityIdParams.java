package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/20.
 */
@Data
public class ActivityIdParams extends UserIdTokenParams {
    private Long activityId;

    public ActivityIdParams(Long activityId) {
        this.activityId = activityId;
    }
}
