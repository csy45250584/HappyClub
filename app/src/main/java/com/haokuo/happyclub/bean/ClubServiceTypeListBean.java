package com.haokuo.happyclub.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/10/9.
 */
@Data
public class ClubServiceTypeListBean {

    private List<ClubServiceTypeBean> data;

    @Data
    public static class ClubServiceTypeBean {
        private long id;
        private String servicelist_code;
        private String servicelist_name;
        private int state;
        private boolean isSelected;

        public ClubServiceTypeBean() {
        }

        public ClubServiceTypeBean(String serviceListName) {
            servicelist_name = serviceListName;
        }
    }
}
