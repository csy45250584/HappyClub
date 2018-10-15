package com.haokuo.happyclub.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/10/13.
 */
@Data
public class OrderDetailBean implements Serializable{
    public static final int ORDER_TYPE_SERVICE = 0;
    public static final int ORDER_TYPE_CANTEEN = 1;
    public static final int ORDER_TYPE_MALL = 2;

    public static final int PAY_TYPE_SCORE = 0;

    public static final int STATE_WAIT_FOR_HANDLE = 1;
    public static final int STATE_PAYED = 2;
    public static final int STATE_ORDER_RECEIVED = 3;
    public static final int STATE_SERVING = 4;
    public static final int STATE_SERVED = 5;
    public static final int STATE_COMPLETED = 6;
    public static final int STATE_CANCELED = 8;

    private String createDate; //下单时间
    private String creator; //下单人昵称
    private long customId; //下单人Id
    private String finalTime; //最后操作时间
    private long id; //订单ID
    private int integralSum; //总积分
    private double moneySum; //总金额
    private int orderType; //订单类型 GetOrderDetailParams中
    private int payType; //支付方式  仅针对下单的字段 0:积分,1:支付宝,2:微信,3:银联,4:积分+其他,
    private String orderNo; //订单号
    private String scheduledTime; //预计用餐时间
    private String remarks; //备注
    private int status; //订单状态
    private List<OrderItem> orderItems; //订单内容
    private AddressResultBean address; //地址

    @Data
    public static class OrderItem implements Serializable {
        private int count; //数量
        private long id; //id
        private int proIntegral; //单个积分
        private String proName; //名称
        private String proPictureurl; //图片
        private double proPrice; //价格
    }

    public String getStateString() {
        switch (status) {
            case STATE_WAIT_FOR_HANDLE:
                return "等待支付";
            case STATE_PAYED:
                return "已付款";
            case STATE_ORDER_RECEIVED:
                return "已接单";
            case STATE_SERVING:
                return "服务中";
            case STATE_SERVED:
                return "已服务";
            case STATE_COMPLETED:
                return "已完成";
            case STATE_CANCELED:
                return "已取消";
            default:
                return null;
        }
    }
}
