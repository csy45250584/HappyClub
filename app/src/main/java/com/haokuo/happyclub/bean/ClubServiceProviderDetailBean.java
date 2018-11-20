package com.haokuo.happyclub.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/10/25.
 */
@Data
public class ClubServiceProviderDetailBean {

    private BusinessBean business;
    private List<ClubServiceBean> serviceList;

    @Data
    public static class BusinessBean {
        private String areaName;
        private String businessName;
        private long deptId;
        private long areaId;
        private long id;
        private String image;
        private String introduction;
        private String managerName;
        private String managerPhone;
        private long managerUserId;
    }


}
