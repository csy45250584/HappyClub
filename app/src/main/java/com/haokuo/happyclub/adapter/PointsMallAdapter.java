package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.ClubServiceBean;
import com.haokuo.happyclub.bean.MallProductBean;
import com.haokuo.happyclub.network.UrlConfig;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class PointsMallAdapter extends BaseQuickAdapter<MallProductBean, BaseViewHolder> {

    public PointsMallAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MallProductBean item) {
        ImageView clubServicePic = helper.getView(R.id.iv_club_service_pic);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getProduct_pictureurl())).into(clubServicePic);
        helper.setText(R.id.tv_club_service_name, item.getProduct_name());
        helper.setText(R.id.tv_club_service_description, item.getDescription());
        helper.setText(R.id.tv_service_score, String.valueOf(item.getProduct_integral()));
    }
}
