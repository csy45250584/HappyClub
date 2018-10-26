package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/10/25.
 */
@Data
public class ClubServiceDetailBean {

    private BusinessBean business;
    private ServiceBean service;

    @Data
    public static class BusinessBean {

        private String businessName;
        private long deptId;
        private long id;
        private String image;
        private String introduction;
        private String managerName;
        private String managerPhone;
        private long managerUserId;
    }

    @Data
    public static class ServiceBean {

        private long business_id;
        private int count;
        private String description;
        private long id;
        private int service_integral;
        private String service_name;
        private String service_pictureurl;
        private long servicelist_id;
        private String servicelist_name;
        private int state;
    }
}
