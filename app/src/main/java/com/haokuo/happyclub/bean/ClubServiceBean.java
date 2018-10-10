package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/10/9.
 */
@Data
public class ClubServiceBean {

    private long business_id;
    private int count;
    private long id;
    private int isSell;
    private int sales_status;
    private String service_name;
    private String description;
    private double service_price;
    private int service_integral;
    private String service_pictureurl;
    private long servicelist_id;
    private String servicelist_name;
    private int state;
}
