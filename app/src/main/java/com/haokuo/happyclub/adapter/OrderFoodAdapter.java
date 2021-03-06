package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.CartFoodBean;
import com.haokuo.happyclub.network.UrlConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class OrderFoodAdapter extends BaseQuickAdapter<CartFoodBean, BaseViewHolder> {

    public OrderFoodAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CartFoodBean item) {
        ImageView ivFoodPic = helper.getView(R.id.iv_food_pic);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getPic())).into(ivFoodPic);
        helper.setText(R.id.tv_food_name, item.getName());
        helper.setText(R.id.tv_food_count, String.format("X%d", item.getCount()));
        DecimalFormat decimalFormat = new DecimalFormat("¥0.00");
        String price = decimalFormat.format(BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getCount())));
        helper.setText(R.id.tv_sum_price, price);
        helper.setText(R.id.tv_food_score, String.valueOf(item.getScore() * item.getCount()));
    }
}
