package com.haokuo.happyclub.adapter;

import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.RecourseBean;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class RecourseAdapter extends BaseQuickAdapter<RecourseBean, BaseViewHolder> {

    public RecourseAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RecourseBean item) {
        helper.setText(R.id.tv_serve_name, item.getServeName());
        helper.setText(R.id.tv_serve_request, item.getClaim());
        helper.setText(R.id.tv_serve_address, item.getAddress());
        helper.setText(R.id.tv_score, "积分:" + item.getIntegral());
        helper.addOnClickListener(R.id.tv_delete_recourse);
        helper.addOnClickListener(R.id.tv_right_button);
        int reviewState = item.getReviewState();
        helper.setGone(R.id.tv_delete_recourse, reviewState == 0 || reviewState == 1 || reviewState == 2 || reviewState == 88);
        switch (reviewState) {
            case 0:
                helper.setText(R.id.tv_review_state, "未审核");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorText2));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 1:
                helper.setText(R.id.tv_review_state, "已审核");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 2:
                helper.setText(R.id.tv_review_state, "已拒绝");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, android.R.color.holo_red_light));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "重新求助");
                break;
            case 11:
                helper.setText(R.id.tv_review_state, "已接单");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 22:
                helper.setText(R.id.tv_review_state, "服务中");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 33:
                helper.setText(R.id.tv_review_state, "已服务");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "完成");
                break;
            case 44:
                helper.setText(R.id.tv_review_state, "已服务");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "评价");
                break;
            case 55:
                helper.setText(R.id.tv_review_state, "已评价");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorPrimary));
                helper.setGone(R.id.tv_right_button, false);
                break;
            case 88:
                helper.setText(R.id.tv_review_state, "已放弃");
                helper.setTextColor(R.id.tv_review_state, ContextCompat.getColor(mContext, R.color.colorText2));
                helper.setGone(R.id.tv_right_button, true);
                helper.setText(R.id.tv_right_button, "重新求助");
                break;
        }
        //        0:未审核; 1:已审核; 2:已拒绝; 11:已接单; 22:服务中; 33:已服务; 44:已完成;88:已放弃
        //                2 88 重新求助
        //                33 完成
        //                44 评价
        //                0 1 2 88 删除求助
    }
}
