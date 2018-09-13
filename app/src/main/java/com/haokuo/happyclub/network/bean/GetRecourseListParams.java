package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

/**
 * Created by zjf on 2018/9/13.
 */
public class GetRecourseListParams extends PageParams {
    private String userTel;

    public GetRecourseListParams(String userTel) {
        this.userTel = userTel;
    }
}
