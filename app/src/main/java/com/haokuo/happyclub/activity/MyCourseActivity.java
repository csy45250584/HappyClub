package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.SchoolAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.CourseBean;
import com.haokuo.happyclub.bean.list.CourseListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.base.PageParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/23.
 */
public class MyCourseActivity extends BaseActivity {
    @BindView(R.id.rv_my_course)
    RecyclerView mRvMyCourse;
    @BindView(R.id.srl_my_course)
    SmartRefreshLayout mSrlMyCourse;
    private SchoolAdapter mSchoolAdapter;
    private PageParams mParams;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_my_course;
    }

    @Override
    protected void initData() {
        mRvMyCourse.setLayoutManager(new LinearLayoutManager(this));
        mRvMyCourse.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mSchoolAdapter = new SchoolAdapter(R.layout.item_school);
        mRvMyCourse.setAdapter(mSchoolAdapter);
        mParams = new PageParams();
    }
    @Override
    protected void loadData() {
        mSrlMyCourse.autoRefresh();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }
    @Override
    protected void initListener() {
        mSrlMyCourse.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<CourseBean>
                (mSrlMyCourse, mParams, mSchoolAdapter, CourseListBean.class, "获取我的课程失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getMyCourseList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getMyCourseList(mParams, mRefreshCallback);
            }
        });
        mSchoolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean item = mSchoolAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(MyCourseActivity.this, SchoolDetailActivity.class);
                    intent.putExtra(SchoolDetailActivity.EXTRA_COURSE_ID, item.getId());
                    startActivityForResult(intent,0);
                }
            }
        });
    }
}
