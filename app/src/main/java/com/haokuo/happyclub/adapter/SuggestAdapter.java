package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.RepairBean;
import com.haokuo.happyclub.bean.SuggestBean;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class SuggestAdapter extends BaseQuickAdapter<SuggestBean, BaseViewHolder> {

    public SuggestAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SuggestBean item) {
        helper.setText(R.id.tv_report_name, item.getUserName());
        helper.setText(R.id.tv_report_content, item.getReportContent());
        helper.setText(R.id.tv_report_time, item.getReportTime());
        switch (item.getState()) {
            case RepairBean.STATE_UNHANDLED:
                helper.setText(R.id.tv_repair_state, "未处理");
                helper.setTextColor(R.id.tv_repair_state, 0xFFFD5722);
                break;
            case RepairBean.STATE_HANDLED:
                helper.setText(R.id.tv_repair_state, "已处理");
                helper.setTextColor(R.id.tv_repair_state, 0xFF32A5A6);
                break;
        }
    }
}
