package com.haokuo.happyclub.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/10/9.
 */
@Data
public class MallProductTypeListBean {

    private List<MallProductTypeBean> data;

    @Data
    public static class MallProductTypeBean {
        private long id;
        private String productlist_code;
        private String productlist_name;
        private int state;
        private boolean isSelected;

        public MallProductTypeBean() {
        }

        public MallProductTypeBean(String productListName) {
            productlist_name = productListName;
        }
    }
}
