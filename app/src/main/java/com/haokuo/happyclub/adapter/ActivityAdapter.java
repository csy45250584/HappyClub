package com.haokuo.happyclub.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.NewsBean;
import com.haokuo.happyclub.network.UrlConfig;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class ActivityAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public ActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NewsBean item) {
        helper.setText(R.id.tv_activity_title, item.getTitle());
        helper.setText(R.id.tv_activity_summary, item.getSummary());
        helper.setText(R.id.tv_activity_time, item.getPublishTime());
        ImageView ivPicActivity = helper.getView(R.id.iv_pic_activity);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getImage())).into(ivPicActivity);
    }
}
