package com.haokuo.happyclub.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.VolunteerActivityBean;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.util.ResUtils;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class VolunteerActivityAdapter extends BaseQuickAdapter<VolunteerActivityBean, BaseViewHolder> {

    public VolunteerActivityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VolunteerActivityBean item) {
        ImageView ivActivityPic = helper.getView(R.id.iv_activity_pic);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getImage())).into(ivActivityPic);
        helper.setText(R.id.tv_activity_name, item.getActivityName());
        helper.setText(R.id.tv_activity_description, item.getDescription());
        helper.setText(R.id.tv_activity_duration, String.format("%s~%s", item.getStartTime(), item.getEndTime()));
        helper.setText(R.id.tv_score, String.valueOf(item.getCredit()));
        boolean isFinished = item.getStatus() == VolunteerActivityBean.STATUS_FINISHED;
        helper.setGone(R.id.tv_activity_finished, isFinished);
        helper.setGone(R.id.tv_sign_up_count, !isFinished);
        if(!isFinished) {
            String count = String.valueOf(item.getCount());
            String joinCount = String.valueOf(item.getJoinCount());
            SpannableString spannableString = new SpannableString(joinCount + "/" + count);
            spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.colorPrimary)), 0, joinCount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_sign_up_count, spannableString);
        }
    }
}
