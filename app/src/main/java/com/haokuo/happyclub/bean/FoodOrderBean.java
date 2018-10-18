package com.haokuo.happyclub.bean;

import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/9/27.
 */
@Data
public class FoodOrderBean {
    private String orderNo;        //订单编号
    private int orderType;        //订单类型
    private Double moneySum;    //金额总计 1
    private int state;  //餐具数量 1
    private Integer integralSum;    //积分总计 1

    private Integer payType;        //支付方式 1
    private int customId;        //客户id
    private int shopId;            //商家id
    private int workId;            //员工id
    private String customName;  //客户名称(显示)
    private int status;            //订单状态
    private String remarks;        //备注 1
    private String finalTime;    //订单时间

    private List<CartFoodBean> orderItems;        //订单菜品内容

    private Long addressId;        //地址id 1
    private String scheduledTime;    //预定时间(就餐时间) 1

    @Data
    public static class CartFoodBean {
        private Long proId;    //项目id
        private Integer count;    //数量

        public CartFoodBean(Long proId, Integer count) {
            this.proId = proId;
            this.count = count;
        }

        public CartFoodBean() {
        }
    }
}
