package com.haokuo.happyclub.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
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

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by zjf on 2018/9/28.
 */
@RuntimePermissions
public class VolunteerOrderActivity extends BaseActivity {
    private static final String TAG = "VolunteerOrderActivity";
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
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private DistanceSearch mDistanceSearch;

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
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            mLocationClient = null;
        }
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
        //定位初始化
        VolunteerOrderActivityPermissionsDispatcher.initLocationClientWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void initLocationClient() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //初始化测距
        mDistanceSearch = new DistanceSearch(this);
        if (mLocationClient != null) {
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            //可在其中解析amapLocation获取相应内容。
                            LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                            List<LatLonPoint> latLonPointList = mVolunteerServeAdapter.getLatLonPointList();
                            if (latLonPointList.size() > 0) {
                                DistanceSearch.DistanceQuery distanceQuery = new DistanceSearch.DistanceQuery();
                                distanceQuery.setOrigins(latLonPointList);
                                distanceQuery.setDestination(latLonPoint);
                                mDistanceSearch.calculateRouteDistanceAsyn(distanceQuery);
                            }
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:"
                                    + aMapLocation.getErrorCode() + ", errInfo:"
                                    + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
        }
        if (mDistanceSearch != null) {
            mDistanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
                @Override
                public void onDistanceSearched(DistanceResult distanceResult, int errorCode) {
                    if (errorCode == 1000) {
                        Log.v("MY_CUSTOM_TAG", "VolunteerOrderActivity onDistanceSearched()-->");
                        for (int i = 0; i < distanceResult.getDistanceResults().size(); i++) {
                            RecourseBean recourseBean = mVolunteerServeAdapter.getData().get(i);
                            if (recourseBean != null) {
                                recourseBean.setDistance(distanceResult.getDistanceResults().get(i).getDistance());
                            }
                        }
                        mVolunteerServeAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "onDistanceSearched: " + "errorCode = " + errorCode);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        VolunteerOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onLocationDenied() {
        new AlertDialog.Builder(this)
                .setTitle("权限提示")
                .setMessage("定位权限被拒绝后无法使用定位功能，是否再次请求权限？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VolunteerOrderActivityPermissionsDispatcher.initLocationClientWithPermissionCheck(VolunteerOrderActivity.this);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
}
