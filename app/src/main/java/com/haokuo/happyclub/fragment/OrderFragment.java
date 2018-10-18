package com.haokuo.happyclub.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.ClubServiceDetailActivity;
import com.haokuo.happyclub.activity.FoodOrderDetailActivity;
import com.haokuo.happyclub.activity.MallOrderDetailActivity;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.adapter.OrderFilterAdapter;
import com.haokuo.happyclub.adapter.OrderListAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.OrderDetailBean;
import com.haokuo.happyclub.bean.OrderFilterBean;
import com.haokuo.happyclub.bean.list.OrderDetailListBean;
import com.haokuo.happyclub.consts.OrderParams;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetOrderListParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/11.
 */
public class OrderFragment extends BaseLazyLoadFragment {
    @BindView(R.id.drop_down_menu)
    DropDownMenu mDropDownMenu;
    RecyclerView mRvOrderList;
    SmartRefreshLayout mSrlOrderList;
    private OrderListAdapter mOrderListAdapter;
    private OrderFilterAdapter mTypeFilterAdapter;
    private OrderFilterAdapter mSortFilterAdapter;
    private Integer mType;
    private Integer mState;
    private GetOrderListParams mParams;

    @Override
    protected void initData() {
        mType = 0;
        mState = null;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.order_list_content, null);
        mRvOrderList = contentView.findViewById(R.id.rv_order_list);
        mSrlOrderList = contentView.findViewById(R.id.srl_order_list);
        //设置服务列表
        mRvOrderList.setLayoutManager(new LinearLayoutManager(mContext));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvOrderList.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mOrderListAdapter = new OrderListAdapter(R.layout.item_my_order);
        mRvOrderList.setAdapter(mOrderListAdapter);
        //初始化筛选器
        ArrayList<View> popupViews = new ArrayList<>();
        String[] headers = {"会所服务", "全部"};
        //设置类别列表
        RecyclerView typeRv = new RecyclerView(mContext);
        typeRv.setBackground(new ColorDrawable(ResUtils.getColor(R.color.colorWhite)));
        typeRv.setLayoutManager(new LinearLayoutManager(mContext));
        typeRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        typeRv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mTypeFilterAdapter = new OrderFilterAdapter(R.layout.item_club_service_filter);
        typeRv.setAdapter(mTypeFilterAdapter);
        ArrayList<OrderFilterBean> typeFilterList = new ArrayList<>();
        typeFilterList.add(new OrderFilterBean("会所服务", OrderParams.ORDER_TYPE_SERVICE));
        typeFilterList.add(new OrderFilterBean("幸福食堂", OrderParams.ORDER_TYPE_CANTEEN));
        typeFilterList.add(new OrderFilterBean("积分商城", OrderParams.ORDER_TYPE_MALL));
        mTypeFilterAdapter.setNewData(typeFilterList);
        popupViews.add(typeRv);
        //设置排序列表
        RecyclerView sortRv = new RecyclerView(mContext);
        sortRv.setBackground(new ColorDrawable(ResUtils.getColor(R.color.colorWhite)));
        sortRv.setLayoutManager(new LinearLayoutManager(mContext));
        sortRv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sortRv.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDefBg,
                0, 0));
        mSortFilterAdapter = new OrderFilterAdapter(R.layout.item_club_service_filter);
        sortRv.setAdapter(mSortFilterAdapter);
        ArrayList<OrderFilterBean> sortFilterList = new ArrayList<>();
        sortFilterList.add(new OrderFilterBean("全部", null));
        sortFilterList.add(new OrderFilterBean("待评价", OrderParams.STATE_COMPLETED));
        sortFilterList.add(new OrderFilterBean("退款", OrderParams.STATE_CANCELED));
        mSortFilterAdapter.setNewData(sortFilterList);
        popupViews.add(sortRv);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        //        mParams = new GetClubServiceParams(null, null, null);
        //        mSearchView.setCursorDrawable(R.drawable.search_bar_cursor);
        mParams = new GetOrderListParams(mType, mState, null, null);
    }

    public void changeParams() {
        mParams.setOrderType(mType);
        mParams.setStatus(mState);
    }

    @Override
    protected void loadData() {
        mSrlOrderList.autoRefresh();
    }

    @Override
    protected void initListener() {
        mOrderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderDetailBean item = mOrderListAdapter.getItem(position);
                if (item != null) {
                    switch (item.getOrderType()) {
                        case OrderDetailBean.ORDER_TYPE_SERVICE: {
                            Intent intent = new Intent(mContext, ClubServiceDetailActivity.class);
                            intent.putExtra(ClubServiceDetailActivity.EXTRA_ORDER_ID, item.getId());
                            startActivity(intent);
                        }
                        break;
                        case OrderDetailBean.ORDER_TYPE_CANTEEN: {
                            Intent intent = new Intent(mContext, FoodOrderDetailActivity.class);
                            intent.putExtra(FoodOrderDetailActivity.EXTRA_ORDER_ID, item.getId());
                            startActivity(intent);
                        }
                        break;
                        case OrderDetailBean.ORDER_TYPE_MALL: {
                            Intent intent = new Intent(mContext, MallOrderDetailActivity.class);
                            intent.putExtra(MallOrderDetailActivity.EXTRA_ORDER_ID, item.getId());
                            startActivity(intent);
                        }
                        break;
                    }
                }
            }
        });
        mSrlOrderList.setOnRefreshLoadMoreListener(new MyRefreshLoadMoreListener<OrderDetailBean>
                (mSrlOrderList, mParams, mOrderListAdapter, OrderDetailListBean.class, "获取订单列表失败") {
            @Override
            protected void loadMore() {
                HttpHelper.getInstance().getOrderList(mParams, mLoadMoreCallback);
            }

            @Override
            protected void refresh() {
                HttpHelper.getInstance().getOrderList(mParams, mRefreshCallback);
            }
        });
        mTypeFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderFilterBean item = mTypeFilterAdapter.getItem(position);
                mTypeFilterAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(item.getTitle());
                mDropDownMenu.closeMenu();
                mType = item.getState();
                changeParams();
                mSrlOrderList.autoRefresh();
            }
        });
        mSortFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderFilterBean item = mSortFilterAdapter.getItem(position);
                mSortFilterAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(item.getTitle());
                mDropDownMenu.closeMenu();
                mState = item.getState();
                changeParams();
                mSrlOrderList.autoRefresh();
            }
        });
    }

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_order;
    }

    public boolean isMenuShowing() {
        return mDropDownMenu != null && mDropDownMenu.isShowing();
    }

    public void closeMenu() {
        if (mDropDownMenu != null) {
            mDropDownMenu.closeMenu();
        }
    }
}
