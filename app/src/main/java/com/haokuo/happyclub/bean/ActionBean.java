package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/9/18.
 */
@Data
public class ActionBean {
    private String title;
    private int imageId;
    private Class clz;

    public ActionBean() {
    }

    public ActionBean(String title, int imageId) {
        this.title = title;
        this.imageId = imageId;
    }

    public ActionBean(String title, int imageId, Class clz) {
        this.title = title;
        this.imageId = imageId;
        this.clz = clz;
    }
}
