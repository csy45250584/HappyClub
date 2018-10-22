package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.MyWalletDetailBean;
import com.haokuo.happyclub.util.utilscode.TimeUtils;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class MyWalletDetailAdapter extends BaseQuickAdapter<MyWalletDetailBean, BaseViewHolder> {

    public MyWalletDetailAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyWalletDetailBean item) {
        helper.setText(R.id.tv_my_wallet_detail, item.getMessage());
        helper.setText(R.id.tv_wallet_detail_time, TimeUtils.sqlTime2String(item.getCreateDate(), TimeUtils.DEFAULT_FORMAT));
    }
}
