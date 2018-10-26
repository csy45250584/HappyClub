package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ClubServiceAdapter;
import com.haokuo.happyclub.adapter.ClubServiceFilterAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.ClubServiceBean;
import com.haokuo.happyclub.bean.ClubServiceTypeListBean;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.OrderResultBean;
import com.haokuo.happyclub.bean.list.ClubServiceListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetClubServiceParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.KeyboardUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/8.
 */
public class ClubServiceActivity extends BaseActivity {
    private static final String TYPE_ALL = "全部类别";
    private static final String SORT_ALL = "综合排序";
    private static final String SORT_ASCEND = "价格升序";
    private static final String SORT_DESCEND = "价格降序";
    RecyclerView mRvClubService;
    SmartRefreshLayout mSrlClubService;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.drop_down_menu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    private List<ClubServiceTypeListBean.ClubServiceTypeBean> mServiceTypeList;
    private ClubServiceAdapter mClubServiceAdapter;
    private GetClubServiceParams mParams;
    private ClubServiceFilterAdapter mTypeFilterAdapter;
    private ClubServiceFilterAdapter mSortFilterAdapter;
    private Long typeListId;
    private Integer sortStatus;
    private String searchName;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service;
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.getItem(0);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    protected void initData() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.club_service_content, null);
        mRvClubService = contentView.findViewById(R.id.rv_club_service);
        mSrlClubService = contentView.findViewById(R.id.srl_club_service);
        //设置服务列表
        mRvClubService.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvClubService.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mClubServiceAdapter = new ClubServiceAdapter(R.layout.item_club_service);
        mRvClubService.setAdapter(mClubServiceAdapter);
        //初始化筛选器
        ArrayList<View> popupViews = new ArrayList<>();
        String[] headers = {TYPE_ALL, SORT_ALL};
        //设置类别列表
        RecyclerView typeRv = new RecyclerView(this);
        typeRv.setBackground(new ColorDrawable(ResUtils.getColor(R.color.colorWhite)));
        typeRv.setLayoutManager(new LinearLayoutManager(this));
        typeRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        typeRv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mTypeFilterAdapter = new ClubServiceFilterAdapter(R.layout.item_club_service_filter);
        typeRv.setAdapter(mTypeFilterAdapter);
        popupViews.add(typeRv);
        //设置排序列表
        RecyclerView sortRv = new RecyclerView(this);
        sortRv.setBackground(new ColorDrawable(ResUtils.getColor(R.color.colorWhite)));
        sortRv.setLayoutManager(new LinearLayoutManager(this));
        sortRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sortRv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mSortFilterAdapter = new ClubServiceFilterAdapter(R.layout.item_club_service_filter);
        sortRv.setAdapter(mSortFilterAdapter);
        ArrayList<ClubServiceTypeListBean.ClubServiceTypeBean> sortFilterList = new ArrayList<>();
        sortFilterList.add(new ClubServiceTypeListBean.ClubServiceTypeBean(SORT_ALL));
        sortFilterList.add(new ClubServiceTypeListBean.ClubServiceTypeBean(SORT_ASCEND));
        sortFilterList.add(new ClubServiceTypeListBean.ClubServiceTypeBean(SORT_DESCEND));
        mSortFilterAdapter.setNewData(sortFilterList);
        popupViews.add(sortRv);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        mParams = new GetClubServiceParams(null, null, null);
        mSearchView.setCursorDrawable(R.drawable.search_bar_cursor);
    }

    public void changeParams() {
        mParams.setSortStatus(sortStatus);
        mParams.setServicelist(typeListId);
        mParams.setServiceName(searchName);
    }

    @Override
    protected void loadData() {
        HttpHelper.getInstance().getServiceType(new EntityCallback<ClubServiceTypeListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取分类失败," + message);
            }

            @Override
            public void onSuccess(Call call, ClubServiceTypeListBean result) {
                mServiceTypeList = result.getData();
                mServiceTypeList.add(0, new ClubServiceTypeListBean.ClubServiceTypeBean(TYPE_ALL));
                mTypeFilterAdapter.setNewData(mServiceTypeList);
                mSrlClubService.autoRefresh();
            }
        });
    }

    @Override
    protected void initListener() {
        mClubServiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                long id = mClubServiceAdapter.getItem(position).getId();
                Intent intent = new Intent(ClubServiceActivity.this, ClubServiceDetailActivity.class);
                intent.putExtra(ClubServiceDetailActivity.EXTRA_SERVICE_ID, id);
                startActivity(intent);
            }
        });
        mClubServiceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ClubServiceBean item = mClubServiceAdapter.getItem(position);
                if (view.getId() == R.id.tv_exchange) {
                    showLoading("正在下单...");
                    //下单请求
                    FoodOrderBean foodOrderBean = new FoodOrderBean();
                    foodOrderBean.setIntegralSum(item.getService_integral());
                    ArrayList<FoodOrderBean.CartFoodBean> cartFoodBeans = new ArrayList<>();
                    cartFoodBeans.add(new FoodOrderBean.CartFoodBean(item.getId(), 1));
                    foodOrderBean.setOrderItems(cartFoodBeans);
                    HttpHelper.getInstance().insertServiceOrder(foodOrderBean, new EntityCallback<OrderResultBean>() {
                        @Override
                        public void onFailure(Call call, String message) {
                            loadFailed("下单失败，" + message);
                        }

                        @Override
                        public void onSuccess(Call call, final OrderResultBean result) {
                            loadSuccess("下单成功", new LoadingDialog.OnFinishListener() {
                                @Override
                                public void onFinish() {
                                    Intent intent = new Intent(ClubServiceActivity.this, ClubServiceOrderDetailActivity.class);
                                    intent.putExtra(ClubServiceOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
        mSrlClubService.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<ClubServiceBean>
                (mSrlClubService, mParams, mClubServiceAdapter, ClubServiceListBean.class, "获取会所服务列表失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getClubService(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getClubService(mParams, mRefreshCallback);
            }
        });
        mTypeFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ClubServiceTypeListBean.ClubServiceTypeBean item = mTypeFilterAdapter.getItem(position);
                mTypeFilterAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(item.getServicelist_name());
                mDropDownMenu.closeMenu();
                typeListId = item.getId();
                changeParams();
                mSrlClubService.autoRefresh();
            }
        });
        mSortFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSortFilterAdapter.setCheckItem(position);
                String servicelistName = mSortFilterAdapter.getItem(position).getServicelist_name();
                mDropDownMenu.setTabText(servicelistName);
                mDropDownMenu.closeMenu();
                switch (servicelistName) {
                    case SORT_ALL:
                        sortStatus = null;
                        break;
                    case SORT_ASCEND:
                        sortStatus = 2;
                        break;
                    case SORT_DESCEND:
                        sortStatus = 1;
                        break;
                }
                changeParams();
                mSrlClubService.autoRefresh();
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //点击输入法搜索按钮
                searchName = query;
                changeParams();
                mSrlClubService.autoRefresh();
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
                // 搜索框关闭
                searchName = null;
                changeParams();
                mSrlClubService.autoRefresh();
            }
        });
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
                return super.dispatchTouchEvent(ev); //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
            }
        }
        return super.dispatchTouchEvent(ev);
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
}