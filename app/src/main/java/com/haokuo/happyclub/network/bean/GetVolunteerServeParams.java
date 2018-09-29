package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class GetVolunteerServeParams extends PageParams {
    private String serveName;

    public GetVolunteerServeParams(String serveName) {
        this.serveName = serveName;
    }
}
