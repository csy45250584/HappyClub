package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/9/18.
 */
@Data
public class ActionBean {
    private String title;
    private int imageId;

    public ActionBean() {
    }

    public ActionBean(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }
}
