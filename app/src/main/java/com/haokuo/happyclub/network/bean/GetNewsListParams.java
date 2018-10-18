package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/17.
 */
@Data
public class GetNewsListParams  extends PageParams{
    private String title;
    private Integer status;
    private Long sortId;
}
