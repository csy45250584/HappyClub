package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.ActionBean;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class ActionAdapter extends BaseQuickAdapter<ActionBean, BaseViewHolder> {

    public ActionAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ActionBean item) {
        helper.setText(R.id.tv_action_title, item.getTitle());
        helper.setImageResource(R.id.iv_action_icon, item.getImageId());
    }
}
