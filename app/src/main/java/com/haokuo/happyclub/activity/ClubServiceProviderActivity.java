package com.haokuo.happyclub.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.fragment.ClubServiceProviderFragment;
import com.haokuo.happyclub.util.utilscode.FragmentUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/13.
 */
public class ClubServiceProviderActivity extends BaseActivity {

    public static final String EXTRA_MODEL_ID = "com.haokuo.happyclub.extra.EXTRA_MODEL_ID";
    public static final String EXTRA_NAME = "com.haokuo.happyclub.extra.EXTRA_NAME";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.fl_fragment)
    FrameLayout mFlFragment;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service_provider;
    }

    @Override
    protected void initData() {
        long modelId = getIntent().getLongExtra(EXTRA_MODEL_ID, -1);
        String title = getIntent().getStringExtra(EXTRA_NAME);
        mMidTitleBar.setMidTitle(title+"服务");
        ClubServiceProviderFragment fragment = new ClubServiceProviderFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ClubServiceProviderFragment.KEY_SORT_ID, modelId);
        fragment.setArguments(bundle);
        FragmentUtils.addFragment(getSupportFragmentManager(), fragment, R.id.fl_fragment, false);
    }

    @Override
    protected void loadData() {
        //       showLoading("");
        //        //加载标题
        //        HttpHelper.getInstance().getNewsSortList(new EntityCallback<NewsTypeListBean>() {
        //            @Override
        //            public void onFailure(Call call, String message) {
        //                ToastUtils.showShort("加载内容失败");
        //                loadClose();
        //            }
        //
        //            @Override
        //            public void onSuccess(Call call, NewsTypeListBean result) {
        //               loadClose();
        //                List<NewsTypeBean> data = result.getData();
        //                ArrayList<String> titleList = new ArrayList<>();
        //                ArrayList<Fragment> fragments = new ArrayList<>();
        //                for (NewsTypeBean newsTypeBean : data) {
        //                    titleList.add(newsTypeBean.getSortName());
        //                    ClubServiceProviderFragment fragment = new ClubServiceProviderFragment();
        //                    Bundle bundle = new Bundle();
        //                    bundle.putLong(ClubServiceProviderFragment.KEY_SORT_ID, newsTypeBean.getId());
        //                    fragment.setArguments(bundle);
        //                    fragments.add(fragment);
        //                }
        //                String[] titles = new String[titleList.size()];
        //                mIndicatorClubService.setViewPager(mVpClubService, titleList.toArray(titles), ClubServiceProviderActivity.this, fragments);
        //            }
        //        });
    }
}
