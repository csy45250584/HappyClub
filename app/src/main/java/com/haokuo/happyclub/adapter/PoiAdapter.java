package com.haokuo.happyclub.adapter;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class PoiAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {

    public PoiAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PoiItem item) {
        helper.setText(R.id.tv_address_name, item.toString());
        helper.setText(R.id.tv_address_detail, item.getSnippet());
    }
}
