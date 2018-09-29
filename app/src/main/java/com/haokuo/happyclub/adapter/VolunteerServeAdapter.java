package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.RecourseBean;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class VolunteerServeAdapter extends BaseQuickAdapter<RecourseBean, BaseViewHolder> {

    public VolunteerServeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RecourseBean item) {
        helper.setText(R.id.tv_serve_name, item.getServeName());
        helper.setText(R.id.tv_serve_request, item.getClaim());
        helper.setText(R.id.tv_serve_address, item.getAddress());
        //TODO 设置距离

        helper.setText(R.id.tv_score, String.valueOf(item.getIntegral()));
        helper.addOnClickListener(R.id.iv_accept_serve);
    }
}
