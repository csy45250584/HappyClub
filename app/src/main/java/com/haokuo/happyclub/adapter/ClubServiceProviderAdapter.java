package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.ClubServiceProviderBean;
import com.haokuo.happyclub.network.UrlConfig;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class ClubServiceProviderAdapter extends BaseQuickAdapter<ClubServiceProviderBean, BaseViewHolder> {

    public ClubServiceProviderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ClubServiceProviderBean item) {
        ImageView ivPicServiceProvider = helper.getView(R.id.iv_pic_service_provider);
        String[] split = item.getImage().split(",");
        if (split.length > 0) {
            Glide.with(mContext).load(UrlConfig.buildBaseImageUrl(split[0])).into(ivPicServiceProvider);
        }
        helper.setText(R.id.tv_service_provider_name, item.getBusinessName());
        helper.setText(R.id.tv_service_provider_introduction, item.getIntroduction());
    }
}
