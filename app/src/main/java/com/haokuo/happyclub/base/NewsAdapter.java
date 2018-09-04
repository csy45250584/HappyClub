package com.haokuo.happyclub.base;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class NewsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public NewsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
    }
}
