package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.util.utilscode.DecimalUtils;
import com.haokuo.happyclub.util.utilscode.TimeUtils;

import java.util.List;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderDetailBean, BaseViewHolder> {

    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderDetailBean item) {
        helper.setText(R.id.tv_order_no, item.getOrderNo());
        List<OrderDetailBean.OrderItem> orderItems = item.getOrderItems();
        if (orderItems.size() == 0) {
            helper.setText(R.id.tv_order_item_name, "无商品");
        } else if (orderItems.size() > 1) {
            helper.setText(R.id.tv_order_item_name, orderItems.get(0).getProName() + "等" + orderItems.size() + "个商品");
        } else {
            helper.setText(R.id.tv_order_item_name, orderItems.get(0).getProName());
        }
        helper.setText(R.id.tv_order_time, TimeUtils.sqlTime2String(item.getCreateDate()));
        helper.setText(R.id.tv_order_state, item.getStateString());
        String price = DecimalUtils.getMoneyString(item.getMoneySum());
        if (item.getOrderType()!= OrderDetailBean.ORDER_TYPE_CANTEEN) {
        helper.setText(R.id.tv_order_price, item.getIntegralSum() + "积分");
        }else {
            helper.setText(R.id.tv_order_price, price + "/" + item.getIntegralSum() + "积分");
        }
        if (orderItems.size() != 0) {
            ImageView ivOrderPic = helper.getView(R.id.iv_order_pic);
            Glide.with(mContext).load(UrlConfig.buildImageUrl(orderItems.get(0).getProPictureurl())).into(ivOrderPic);
        }
    }
}
