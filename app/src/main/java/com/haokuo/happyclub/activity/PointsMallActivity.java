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
import com.haokuo.happyclub.adapter.MallProductFilterAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.PointsMallAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.FoodOrderBean;
import com.haokuo.happyclub.bean.MallProductBean;
import com.haokuo.happyclub.bean.MallProductTypeListBean;
import com.haokuo.happyclub.bean.OrderResultBean;
import com.haokuo.happyclub.bean.list.MallProductListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetMallProductParams;
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
 * Created by zjf on 2018/10/12.
 */
public class PointsMallActivity extends BaseActivity {
    private static final String TYPE_ALL = "全部类别";
    private static final String SORT_ALL = "综合排序";
    private static final String SORT_ASCEND = "价格升序";
    private static final String SORT_DESCEND = "价格降序";
    @BindView(R.id.drop_down_menu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    private SmartRefreshLayout mSrlPointsMall;
    private RecyclerView mRvPointsMall;
    private PointsMallAdapter mPointsMallAdapter;
    private MallProductFilterAdapter mTypeFilterAdapter;
    private MallProductFilterAdapter mSortFilterAdapter;
    private List<MallProductTypeListBean.MallProductTypeBean> mProductTypeList;
    private GetMallProductParams mParams;
    private Long typeListId;
    private Integer sortStatus;
    private String searchName;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_points_mall;
    }

    @Override
    protected void initData() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.point_mall_content, null);
        mRvPointsMall = contentView.findViewById(R.id.rv_points_mall);
        mSrlPointsMall = contentView.findViewById(R.id.srl_points_mall);
        //设置商品列表
        mRvPointsMall.setLayoutManager(new LinearLayoutManager(this));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvPointsMall.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mPointsMallAdapter = new PointsMallAdapter(R.layout.item_club_service);
        mRvPointsMall.setAdapter(mPointsMallAdapter);

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
        mTypeFilterAdapter = new MallProductFilterAdapter(R.layout.item_club_service_filter);
        typeRv.setAdapter(mTypeFilterAdapter);
        popupViews.add(typeRv);
        //设置排序列表
        RecyclerView sortRv = new RecyclerView(this);
        sortRv.setBackground(new ColorDrawable(ResUtils.getColor(R.color.colorWhite)));
        sortRv.setLayoutManager(new LinearLayoutManager(this));
        sortRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sortRv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mSortFilterAdapter = new MallProductFilterAdapter(R.layout.item_club_service_filter);
        sortRv.setAdapter(mSortFilterAdapter);
        ArrayList<MallProductTypeListBean.MallProductTypeBean> sortFilterList = new ArrayList<>();
        sortFilterList.add(new MallProductTypeListBean.MallProductTypeBean(SORT_ALL));
        sortFilterList.add(new MallProductTypeListBean.MallProductTypeBean(SORT_ASCEND));
        sortFilterList.add(new MallProductTypeListBean.MallProductTypeBean(SORT_DESCEND));
        mSortFilterAdapter.setNewData(sortFilterList);
        popupViews.add(sortRv);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        mParams = new GetMallProductParams(null, null, null);
        mSearchView.setCursorDrawable(R.drawable.search_bar_cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.getItem(0);
        mSearchView.setMenuItem(item);
        return true;
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
    protected void loadData() {
        HttpHelper.getInstance().getMallType(new EntityCallback<MallProductTypeListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取分类失败," + message);
            }

            @Override
            public void onSuccess(Call call, MallProductTypeListBean result) {
                mProductTypeList = result.getData();
                mProductTypeList.add(0, new MallProductTypeListBean.MallProductTypeBean(TYPE_ALL));
                mTypeFilterAdapter.setNewData(mProductTypeList);
                mSrlPointsMall.autoRefresh();
            }
        });
    }

    public void changeParams() {
        mParams.setSortStatus(sortStatus);
        mParams.setProductlistId(typeListId);
        mParams.setProductName(searchName);
    }

    @Override
    protected void initListener() {
        mSrlPointsMall.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<MallProductBean>
                (mSrlPointsMall, mParams, mPointsMallAdapter, MallProductListBean.class, "获取积分商城列表失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getMallProduct(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getMallProduct(mParams, mRefreshCallback);
            }
        });
        mTypeFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MallProductTypeListBean.MallProductTypeBean item = mTypeFilterAdapter.getItem(position);
                mTypeFilterAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(item.getProductlist_name());
                mDropDownMenu.closeMenu();
                typeListId = item.getId();
                changeParams();
                mSrlPointsMall.autoRefresh();
            }
        });
        mSortFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSortFilterAdapter.setCheckItem(position);
                String productlistName = mSortFilterAdapter.getItem(position).getProductlist_name();
                mDropDownMenu.setTabText(productlistName);
                mDropDownMenu.closeMenu();
                switch (productlistName) {
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
                mSrlPointsMall.autoRefresh();
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //点击输入法搜索按钮
                searchName = query;
                changeParams();
                mSrlPointsMall.autoRefresh();
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
                searchName = null;
                changeParams();
                mSrlPointsMall.autoRefresh();
            }
        });
        mPointsMallAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_exchange) { //兑换
                    MallProductBean item = mPointsMallAdapter.getItem(position);
                    showLoading("正在下单...");
                    //下单请求
                    FoodOrderBean foodOrderBean = new FoodOrderBean();
                    foodOrderBean.setIntegralSum(item.getProduct_integral());
                    ArrayList<FoodOrderBean.CartFoodBean> cartFoodBeans = new ArrayList<>();
                    cartFoodBeans.add(new FoodOrderBean.CartFoodBean(item.getId(), 1));
                    foodOrderBean.setOrderItems(cartFoodBeans);
                    HttpHelper.getInstance().insertMallOrder(foodOrderBean, new EntityCallback<OrderResultBean>() {
                        @Override
                        public void onFailure(Call call, String message) {
                            loadFailed("下单失败，" + message);
                        }

                        @Override
                        public void onSuccess(Call call, final OrderResultBean result) {
                            loadSuccess("下单成功", new LoadingDialog.OnFinishListener() {
                                @Override
                                public void onFinish() {
                                    Intent intent = new Intent(PointsMallActivity.this, MallOrderDetailActivity.class);
                                    intent.putExtra(MallOrderDetailActivity.EXTRA_ORDER_ID, result.getOrderId());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    //new NetworkCallback() {
    //        @Override
    //        public void onSuccess(Call call, String json) {
    //
    //        }
    //
    //        @Override
    //        public void onFailure(Call call, String message) {
    //
    //        }
    //    }
    //点击其他地方，输入法消失
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

    //点击其他地方，输入法消失
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
