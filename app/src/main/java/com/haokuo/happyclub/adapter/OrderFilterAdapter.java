package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.OrderFilterBean;
import com.haokuo.happyclub.util.ResUtils;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class OrderFilterAdapter extends BaseQuickAdapter<OrderFilterBean, BaseViewHolder> {
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public OrderFilterAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderFilterBean item) {
        helper.setText(R.id.tv_service_filter, item.getTitle());
        helper.setTextColor(R.id.tv_service_filter, checkItemPosition == helper.getLayoutPosition() ? ResUtils.getColor(R.color.colorPrimary) : ResUtils.getColor(R.color.colorText1));
    }
}
