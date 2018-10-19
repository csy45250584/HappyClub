package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/17.
 */
@Data
public class GetNewsListParams extends PageParams {
    public static final int STATUS_NEWS = 0;
    public static final int STATUS_ACTIVITY = 1;
    private String title;
    private Integer status; //头条:0 活动:1
    private Long sortId; //只针对活动的分类字段(分类查询)

    public GetNewsListParams() {

    }

    public GetNewsListParams(String title, Integer status, Long sortId) {
        this.title = title;
        this.status = status;
        this.sortId = sortId;
    }
}
