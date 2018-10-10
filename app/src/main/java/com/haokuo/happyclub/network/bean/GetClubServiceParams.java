package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/9.
 */
@Data
public class GetClubServiceParams extends PageParams {
    private Long servicelist;
    private String serviceName;
    private Integer sortStatus;

    public GetClubServiceParams(Long servicelist, String serviceName, Integer sortStatus) {
        this.servicelist = servicelist;
        this.serviceName = serviceName;
        this.sortStatus = sortStatus;
    }
}
