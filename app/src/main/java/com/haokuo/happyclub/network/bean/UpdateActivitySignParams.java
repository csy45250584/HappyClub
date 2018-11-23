package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.UserIdTokenParams;

import lombok.Data;

/**
 * Created by zjf on 2018/11/22.
 */
@Data
public class UpdateActivitySignParams extends UserIdTokenParams {
    private Long activityId;
    private Integer signType;

    public UpdateActivitySignParams(Long activityId, Integer signType) {
        this.activityId = activityId;
        this.signType = signType;
    }
}
