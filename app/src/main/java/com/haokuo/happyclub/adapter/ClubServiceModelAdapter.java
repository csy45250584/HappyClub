package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.NewsTypeBean;
import com.haokuo.happyclub.network.UrlConfig;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class ClubServiceModelAdapter extends BaseQuickAdapter<NewsTypeBean, BaseViewHolder> {

    public ClubServiceModelAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NewsTypeBean item) {
        ImageView ivPicServiceProvider = helper.getView(R.id.iv_pic_service_provider);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getImage())).into(ivPicServiceProvider);
        helper.setText(R.id.tv_service_provider_name, item.getSortName());
        helper.setText(R.id.tv_service_provider_introduction, item.getDescription());
    }
}
