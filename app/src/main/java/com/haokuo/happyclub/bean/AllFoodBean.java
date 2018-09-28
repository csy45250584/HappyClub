package com.haokuo.happyclub.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/9/18.
 */
@Data
public class AllFoodBean {
    private List<FoodListBean> data;

    @Data
    public static class FoodBean {
        /**
         * count : 0
         * deleted : false
         * description : 听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示
         * food_integral : 1541
         * food_name : 炒豆芽
         * food_pictureurl : photo/2018-09/20180903161408_288.JPG
         * food_price : 8
         * foodlist_id : 1
         * foodlist_name : 家常菜
         * id : 4
         * isSell : 0
         * sales_status : 1
         */
        private int count;
        private boolean deleted;
        private String description;
        private int food_integral;
        private String food_name;
        private String food_pictureurl;
        private double food_price;
        private long foodlist_id;
        private String foodlist_name;
        private long id;
        private int isSell;
        private int sales_status;
        private int buyCount;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    @Data
    public static class FoodListBean {
        /**
         * deleted : false
         * foodlist_code : 1001
         * foodlist_name : 家常菜
         * foods : [{"count":0,"deleted":false,"description":"听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示","food_integral":1541,"food_name":"炒豆芽","food_pictureurl":"photo/2018-09/20180903161408_288.JPG","food_price":8,"foodlist_id":1,"foodlist_name":"家常菜","id":4,"isSell":0,"sales_status":1},{"count":0,"deleted":false,"description":"听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示听说你要加描述,看你怎么显示","food_integral":1541,"food_name":"拍黄瓜","food_pictureurl":"photo/2018-09/20180903161500_768.JPG","food_price":8,"foodlist_id":1,"foodlist_name":"家常菜","id":5,"isSell":0,"sales_status":1}]
         * id : 1
         * state : 0
         * updateDate : 2018-03-27 16:40:00.0
         * updator : admin
         */

        private String foodlist_code;
        private String foodlist_name;
        private long id;
        private int state;
        private String updateDate;
        private String updator;
        private List<FoodBean> foods;
    }
}
