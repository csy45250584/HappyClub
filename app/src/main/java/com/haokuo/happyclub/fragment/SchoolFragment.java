package com.haokuo.happyclub.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.SchoolDetailActivity;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.SchoolAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.CourseBean;
import com.haokuo.happyclub.bean.list.CourseListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetCourseListParams;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * Created by zjf on 2018/11/9.
 */
public class SchoolFragment extends BaseLazyLoadFragment {
    public static final String KEY_SORT_ID = "key_sort_id";
    @BindView(R.id.rv_school)
    RecyclerView mRvSchool;
    @BindView(R.id.srl_school)
    SmartRefreshLayout mSrlSchool;
    private long mSortId;
    private SchoolAdapter mSchoolAdapter;
    private GetCourseListParams mParams;

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mSortId = getArguments().getLong(KEY_SORT_ID);
        }
        mRvSchool.setLayoutManager(new LinearLayoutManager(mContext));
        mRvSchool.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL));
        mSchoolAdapter = new SchoolAdapter(R.layout.item_school);
        mRvSchool.setAdapter(mSchoolAdapter);
        mParams = new GetCourseListParams(null, mSortId);
    }

    @Override
    protected void loadData() {
        mSrlSchool.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && getUserVisibleHint()) {
            loadData();
        }
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_school;
    }

    @Override
    protected void initListener() {
        //        mSchoolAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
        //            @Override
        //            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //                CourseBean item = mSchoolAdapter.getItem(position);
        //                if (view.getId() == R.id.tv_sign_up && item != null) {
        //                    if (!item.getUserCount().equals(item.getCount())) {
        //                        CourseIdParams params = new CourseIdParams(item.getId());
        //                        mContext.showLoading("预约中...");
        //                        HttpHelper.getInstance().reserveCourse(params, new NetworkCallback() {
        //                            @Override
        //                            public void onSuccess(Call call, String json) {
        //                                mContext.loadSuccess("预约成功",false);
        //                            }
        //
        //                            @Override
        //                            public void onFailure(Call call, String message) {
        //                                mContext.loadFailed("预约失败，" + message);
        //                            }
        //                        });
        //                    }
        //                }
        //            }
        //        });
        mSchoolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseBean item = mSchoolAdapter.getItem(position);
                if (item != null) {
                    Intent intent = new Intent(mContext, SchoolDetailActivity.class);
                    intent.putExtra(SchoolDetailActivity.EXTRA_COURSE_ID, item.getId());
                    startActivityForResult(intent,0);
                }
            }
        });
        mSrlSchool.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<CourseBean>
                (mSrlSchool, mParams, mSchoolAdapter, CourseListBean.class, "获取课程列表失败") {

            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getCourseList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getCourseList(mParams, mRefreshCallback);
            }
        });
    }
}
