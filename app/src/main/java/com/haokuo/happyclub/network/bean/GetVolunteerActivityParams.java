package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class GetVolunteerActivityParams extends PageParams {
    private String activityName;
    private String startTime;
    private String endTime;
    private Integer status;

    public GetVolunteerActivityParams(String activityName) {
        this.activityName = activityName;
        this.status = 2;
    }
}
