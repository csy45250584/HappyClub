package com.haokuo.happyclub.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.VolunteerServeAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.RecourseBean;
import com.haokuo.happyclub.bean.list.RecourseListBean;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.GetVolunteerServeParams;
import com.haokuo.happyclub.network.bean.base.IdParams;
import com.haokuo.happyclub.util.utilscode.KeyboardUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/28.
 */
public class VolunteerOrderActivity extends BaseActivity {
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.rv_volunteer_order)
    RecyclerView mRvVolunteerOrder;
    @BindView(R.id.srl_volunteer_order)
    SmartRefreshLayout mSrlVolunteerOrder;
    private VolunteerServeAdapter mVolunteerServeAdapter;
    private GetVolunteerServeParams mParams;
    private MyRefreshLoadMoreListener<RecourseBean> mSrlListener;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_volunteer_order;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                View rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                rootView.setFocusable(true);
                rootView.setFocusableInTouchMode(true);
                rootView.requestFocus();
                KeyboardUtils.hideSoftInput(this, v);
                //                mSearchView.closeSearch();
                return super.dispatchTouchEvent(ev); //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.getItem(0);
        mSearchView.setMenuItem(item);
        return true;
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void loadData() {
        mSrlVolunteerOrder.autoRefresh();
    }

    @Override
    protected void initListener() {
        mSrlListener = new MyRefreshLoadMoreListener<RecourseBean>
                (mSrlVolunteerOrder, mParams, mVolunteerServeAdapter, RecourseListBean.class, "获取志愿工单失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getVolunteerServeList((GetVolunteerServeParams) getParams(), mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getVolunteerServeList((GetVolunteerServeParams) getParams(), mRefreshCallback);
            }
        };
        mSrlVolunteerOrder.setOnRefreshLoadMoreListener(mSrlListener);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 点击输入法搜索按钮
                mSrlListener.setParams(new GetVolunteerServeParams(query));
                mSrlVolunteerOrder.autoRefresh();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //每次输入新的字符串
                return true;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                // 搜索框关闭
                mParams.resetPageIndex();
                mSrlListener.setParams(mParams);
                mSrlVolunteerOrder.autoRefresh();
            }
        });
        mVolunteerServeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RecourseBean item = mVolunteerServeAdapter.getItem(position);
                if (item != null) {
                    switch (view.getId()) {
                        case R.id.iv_accept_serve://接受服务
                            IdParams idParams = new IdParams(item.getId());
                            showLoading("接受求助中...");
                            HttpHelper.getInstance().acceptServe(idParams, new NetworkCallback() {
                                @Override
                                public void onSuccess(Call call, String json) {
                                    loadSuccess("接受成功", false);
                                    mSrlVolunteerOrder.autoRefresh();
                                }

                                @Override
                                public void onFailure(Call call, String message) {
                                    loadFailed("接受失败，" + message);
                                }
                            });
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        //设置RecyclerView
        mRvVolunteerOrder.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvVolunteerOrder.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider));
        mVolunteerServeAdapter = new VolunteerServeAdapter(R.layout.item_volunteer_serve);
        mRvVolunteerOrder.setAdapter(mVolunteerServeAdapter);
        mParams = new GetVolunteerServeParams(null);
        //设置搜索框指针
        mSearchView.setCursorDrawable(R.drawable.search_bar_cursor);
    }
}
