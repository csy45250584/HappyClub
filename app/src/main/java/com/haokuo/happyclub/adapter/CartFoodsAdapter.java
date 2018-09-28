package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.CartFoodBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class CartFoodsAdapter extends BaseQuickAdapter<CartFoodBean, BaseViewHolder> {

    public CartFoodsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CartFoodBean item) {
        helper.setText(R.id.tv_food_name, item.getName());
        DecimalFormat decimalFormat = new DecimalFormat("¥0.00");
        String price = decimalFormat.format(BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getCount())));
        helper.setText(R.id.tv_sum_price, price);
        helper.addOnClickListener(R.id.iv_delete);
        helper.addOnClickListener(R.id.iv_add);
        helper.setText(R.id.tv_food_count, String.valueOf(item.getCount()));
    }
}
