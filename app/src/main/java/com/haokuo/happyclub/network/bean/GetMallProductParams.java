package com.haokuo.happyclub.network.bean;

import com.haokuo.happyclub.network.bean.base.PageParams;

import lombok.Data;

/**
 * Created by zjf on 2018/10/9.
 */
@Data
public class GetMallProductParams extends PageParams {
    private Long productlistId;
    private String productName;
    private Integer sortStatus;

    public GetMallProductParams(Long productlistId, String productName, Integer sortStatus) {
        this.productlistId = productlistId;
        this.productName = productName;
        this.sortStatus = sortStatus;
    }
}
