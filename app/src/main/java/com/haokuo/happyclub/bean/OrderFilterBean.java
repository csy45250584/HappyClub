package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/10/15.
 */
@Data
public class OrderFilterBean {
    private String title;
    private Integer state;

    public OrderFilterBean(String title, Integer state) {
        this.title = title;
        this.state = state;
    }

    public OrderFilterBean() {
    }
}
