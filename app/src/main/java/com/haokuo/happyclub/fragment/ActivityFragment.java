package com.haokuo.happyclub.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ActivityAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.NewsTypeBean;
import com.haokuo.happyclub.bean.list.NewsTypeListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.utilscode.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/11.
 */
public class ActivityFragment extends BaseLazyLoadFragment {

    @BindView(R.id.indicator_activity)
    SlidingTabLayout mIndicatorActivity;
    @BindView(R.id.vp_activity)
    ViewPager mVpActivity;
    private ActivityAdapter mActivityAdapter;

    @Override
    protected void initData() {
        //        mRvActivity.setLayoutManager(new LinearLayoutManager(mContext));
        //        mRvActivity.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        //        mActivityAdapter = new ActivityAdapter(R.layout.item_activity);
        //        mRvActivity.setAdapter(mActivityAdapter);
        //        ArrayList<RecourseBean> recourseBeans = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            recourseBeans.add(new RecourseBean());
        //        }
        //        mActivityAdapter.setNewData(recourseBeans);
    }


    @Override
    protected void loadData() {
        mContext.showLoading("");
        //加载标题
        HttpHelper.getInstance().getNewsSortList(new EntityCallback<NewsTypeListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("加载内容失败");
                mContext.loadClose();
            }

            @Override
            public void onSuccess(Call call, NewsTypeListBean result) {
                mContext.loadClose();
                List<NewsTypeBean> data = result.getData();
                ArrayList<String> titleList = new ArrayList<>();
                ArrayList<Fragment> fragments = new ArrayList<>();
                for (NewsTypeBean newsTypeBean : data) {
                    titleList.add(newsTypeBean.getSortName());
                    SingleActivityFragment fragment = new SingleActivityFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong(SingleActivityFragment.KEY_SORT_ID, newsTypeBean.getId());
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                String[] titles = new String[titleList.size()];
                mIndicatorActivity.setViewPager(mVpActivity, titleList.toArray(titles), mContext, fragments);
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_activity;
    }
}
