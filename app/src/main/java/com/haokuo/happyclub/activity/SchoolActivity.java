package com.haokuo.happyclub.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.CourseTypeBean;
import com.haokuo.happyclub.bean.list.CourseTypeListBean;
import com.haokuo.happyclub.fragment.SchoolFragment;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/9.
 */
public class SchoolActivity extends BaseActivity {
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.indicator_school)
    SlidingTabLayout mIndicatorSchool;
    @BindView(R.id.vp_school)
    ViewPager mVpSchool;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_school;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        showLoading("");
        //加载标题
        HttpHelper.getInstance().getCourseListType(new EntityCallback<CourseTypeListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("加载内容失败");
                loadClose();
            }

            @Override
            public void onSuccess(Call call, CourseTypeListBean result) {
                loadClose();
                List<CourseTypeBean> data = result.getData();
                ArrayList<String> titleList = new ArrayList<>();
                mFragments = new ArrayList<>();

                for (CourseTypeBean courseTypeBean : data) {
                    titleList.add(courseTypeBean.getCourselistName());
                    SchoolFragment fragment = new SchoolFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong(SchoolFragment.KEY_SORT_ID, courseTypeBean.getId());
                    fragment.setArguments(bundle);
                    mFragments.add(fragment);
                }
                String[] titles = new String[titleList.size()];
                mIndicatorSchool.setViewPager(mVpSchool, titleList.toArray(titles), SchoolActivity.this, mFragments);
            }
        });
    }
}
