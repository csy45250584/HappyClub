package com.haokuo.happyclub.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.util.ResUtils;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class TablewareAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private int mSelectCount;

    public TablewareAdapter(int layoutResId, int tableWareCount) {
        super(layoutResId);
        mSelectCount = tableWareCount;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Integer item) {
        helper.setTextColor(R.id.tv_tableware_count, mSelectCount == item ? ResUtils.getColor(R.color.colorPrimary) : ResUtils.getColor(R.color.colorText1));
        helper.setText(R.id.tv_tableware_count, getText(item));
    }

    public String getText(Integer item) {
        switch (item) {
            case 0:
                return "不需要餐具";
            case 100:
                return "10人以上";
            default:
                return item + "人";
        }
    }
}
