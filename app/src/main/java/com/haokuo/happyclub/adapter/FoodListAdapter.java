package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.AllFoodBean;
import com.haokuo.happyclub.network.UrlConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class FoodListAdapter extends BaseQuickAdapter<AllFoodBean.FoodBean, BaseViewHolder> {

    public FoodListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AllFoodBean.FoodBean item) {
        helper.setText(R.id.tv_food_name, item.getFood_name());
        helper.setText(R.id.tv_food_description, item.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("¥0.00");
        String price = decimalFormat.format(BigDecimal.valueOf(item.getFood_price()));
        helper.setText(R.id.tv_food_price, price);
        helper.setText(R.id.tv_food_score, String.valueOf(item.getFood_integral()));
        ImageView ivFoodPic = helper.getView(R.id.iv_food_pic);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getFood_pictureurl())).into(ivFoodPic);
        helper.setGone(R.id.tv_food_count, item.getBuyCount() != 0);
        helper.setText(R.id.tv_food_count, String.valueOf(item.getBuyCount()));
        helper.setGone(R.id.iv_delete, item.getBuyCount() != 0);
        helper.addOnClickListener(R.id.iv_delete);
        helper.addOnClickListener(R.id.iv_add);
    }

    public int getItemPositionById(long foodId) {
        for (int i = 0; i < mData.size(); i++) {
            AllFoodBean.FoodBean foodBean = mData.get(i);
            if (foodBean.getId() == foodId) {
                return i;
            }
        }
        return -1;
    }

    public void deleteAllBuyCount() {
        for (int i = 0; i < mData.size(); i++) {
            AllFoodBean.FoodBean foodBean = mData.get(i);
            if (foodBean.getBuyCount()!=0) {
                foodBean.setBuyCount(0);
                notifyItemChanged(i);
            }
        }
    }
}
