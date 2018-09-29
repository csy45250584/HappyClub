package com.haokuo.happyclub.adapter;

import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.network.bean.GetAcceptedServeParams;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class MyServeAdapter extends BaseQuickAdapter<RecourseBean, BaseViewHolder> {

    public MyServeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RecourseBean item) {
        helper.setText(R.id.tv_serve_name, item.getServeName());
        helper.setText(R.id.tv_serve_request, item.getClaim());
        helper.setText(R.id.tv_serve_address, item.getAddress());
        helper.setText(R.id.tv_score, "积分:" + item.getIntegral());
        helper.addOnClickListener(R.id.tv_delete_serve);
        helper.addOnClickListener(R.id.tv_right_button);
        int reviewState = item.getReviewState();
        helper.setGone(R.id.tv_delete_serve, item.getReviewState().equals(GetAcceptedServeParams.STATUS_RECEIVED));
        switch (reviewState) {
            case 11:
                helper.setText(R.id.tv_review_state, "已接单");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "去服务");
                break;
            case 22:
                Log.v("MY_CUSTOM_TAG", "MyServeAdapter convert()-->" );
                helper.setText(R.id.tv_review_state, "服务中");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "完成");
                break;
            case 33:
                helper.setText(R.id.tv_review_state, "已服务");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 44:
                helper.setText(R.id.tv_review_state, "已服务");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 88:
                helper.setText(R.id.tv_review_state, "已放弃");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorText2));
                helper.setGone(R.id.tv_right_button, false);
                break;
        }
    }
}
