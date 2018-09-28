package com.haokuo.happyclub.bean;

import lombok.Data;

/**
 * Created by zjf on 2018/9/27.
 */
@Data
public class FoodOrderBean {
    private String orderNo;        //订单编号
    private int orderType;        //订单类型
    private Double moneySum;    //金额总计
    private int payType;        //支付方式
    private int customId;        //客户id 我的ID
    private int shopId;            //商家id
    private int workId;            //员工id
    private String customName;  //客户名称(显示)
    private int status;            //订单状态
    private String remarks;        //备注
    private String finalTime;    //订单时间
    private Long addresId;        //地址id
    private CartFoodBean orderItems;      //订单菜品内容
}
