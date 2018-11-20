package com.haokuo.happyclub.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.NewsTypeBean;
import com.haokuo.happyclub.bean.list.NewsTypeListBean;
import com.haokuo.happyclub.fragment.ClubServiceProviderFragment;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.utilscode.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/13.
 */
public class ClubServiceProviderActivity extends BaseActivity {
    @BindView(R.id.indicator_club_service)
    SlidingTabLayout mIndicatorClubService;
    @BindView(R.id.vp__club_service)
    ViewPager mVpClubService;
    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service_provider;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
       showLoading("");
        //加载标题
        HttpHelper.getInstance().getNewsSortList(new EntityCallback<NewsTypeListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("加载内容失败");
                loadClose();
            }

            @Override
            public void onSuccess(Call call, NewsTypeListBean result) {
               loadClose();
                List<NewsTypeBean> data = result.getData();
                ArrayList<String> titleList = new ArrayList<>();
                ArrayList<Fragment> fragments = new ArrayList<>();
                for (NewsTypeBean newsTypeBean : data) {
                    titleList.add(newsTypeBean.getSortName());
                    ClubServiceProviderFragment fragment = new ClubServiceProviderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong(ClubServiceProviderFragment.KEY_SORT_ID, newsTypeBean.getId());
                    fragment.setArguments(bundle);
                    fragments.add(fragment);
                }
                String[] titles = new String[titleList.size()];
                mIndicatorClubService.setViewPager(mVpClubService, titleList.toArray(titles), ClubServiceProviderActivity.this, fragments);
            }
        });
    }
}
