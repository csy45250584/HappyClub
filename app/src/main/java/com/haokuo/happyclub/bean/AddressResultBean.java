package com.haokuo.happyclub.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by zjf on 2018/9/13.
 */
@Data
public class AddressResultBean implements Serializable{
    private long id;
    private String name;
    private String telphone;
    private String area;
    private String address;
    private String zip;
    private boolean defaultFlag;

    public AddressResultBean(String name, String telphone, String area, String address, String zip, boolean defaultFlag) {
        this.name = name;
        this.telphone = telphone;
        this.area = area;
        this.address = address;
        this.zip = zip;
        this.defaultFlag = defaultFlag;
    }

    public AddressResultBean() {
    }
}
