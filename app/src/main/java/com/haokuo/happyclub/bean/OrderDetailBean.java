package com.haokuo.happyclub.bean;

import com.haokuo.happyclub.consts.OrderParams;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by zjf on 2018/10/13.
 */
@Data
public class OrderDetailBean implements Serializable {
    public static final int ORDER_TYPE_SERVICE = 0;
    public static final int ORDER_TYPE_CANTEEN = 1;
    public static final int ORDER_TYPE_MALL = 2;

    public static final int PAY_TYPE_SCORE = 0;

    public static final int STATE_WAIT_FOR_HANDLE = 1;
    public static final int STATE_PAYED = 2;
    public static final int STATE_ORDER_RECEIVED = 3;
    public static final int STATE_SERVING = 4;
    public static final int STATE_SERVED = 5;
    public static final int STATE_NO_EVALUATE = 6;
    public static final int STATE_COMPLETED = 9;
    private static final int STATE_ORDER_DISTRIBUTED = 33;
    private static final int STATE_APPLY_REFUND = 11;
    private static final int STATE_REFUND_COMPLETED = 7;
    private static final int STATE_REFUND_FAILED = 77;
    private static final int STATE_REFUND_COMPLAINT = 78;
    private static final int STATE_INVALID = 8;

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

        public OrderItem() {
        }

        public OrderItem(int count, long id, int proIntegral, String proName, String proPictureurl, double proPrice) {
            this.count = count;
            this.id = id;
            this.proIntegral = proIntegral;
            this.proName = proName;
            this.proPictureurl = proPictureurl;
            this.proPrice = proPrice;
        }
    }

    public String getStateString() {
        switch (status) {
            case STATE_WAIT_FOR_HANDLE:
                return "待付款";
            case STATE_PAYED:
                return "已付款";
            case STATE_ORDER_RECEIVED:
                return "已接单";
            case STATE_ORDER_DISTRIBUTED:
                return "已分配";
            case STATE_SERVING:
                if (orderType == OrderParams.ORDER_TYPE_SERVICE) {
                    return "服务中";
                } else {
                    return "派送中";
                }
            case STATE_SERVED:
                if (orderType == OrderParams.ORDER_TYPE_SERVICE) {
                    return "已服务";
                } else {
                    return "已派送";
                }
            case STATE_NO_EVALUATE:
                return "待评价";
            case STATE_COMPLETED:
                return "已完成";
            case STATE_APPLY_REFUND:
                return "退款申请中";
            case STATE_REFUND_COMPLETED:
                return "退款完成";
            case STATE_REFUND_FAILED:
                return "退款失败";
            case STATE_REFUND_COMPLAINT:
                return "退款申诉中";
            case STATE_INVALID:
                return "已失效";
            default:
                return "未知状态:" + status;
        }
    }
}
