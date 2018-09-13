package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.AddressResultBean;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class AddressAdapter extends BaseQuickAdapter<AddressResultBean, BaseViewHolder> {

    public AddressAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AddressResultBean item) {
        helper.addOnClickListener(R.id.btn_modify);
        helper.addOnClickListener(R.id.btn_delete);
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_tel, item.getTelphone());
        helper.setText(R.id.tv_address, item.getArea() + " " + item.getAddress());
        helper.setGone(R.id.tv_default_flag, item.isDefaultFlag());
    }
}
