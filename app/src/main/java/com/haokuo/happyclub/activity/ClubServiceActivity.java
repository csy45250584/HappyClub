package com.haokuo.happyclub.activity;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.ClubServiceAdapter;
import com.haokuo.happyclub.adapter.ClubServiceFilterAdapter;
import com.haokuo.happyclub.adapter.MyRefreshLoadMoreListener;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.ClubServiceBean;
import com.haokuo.happyclub.bean.ClubServiceTypeListBean;
import com.haokuo.happyclub.bean.list.ClubServiceListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetClubServiceParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.haokuo.midtitlebar.MidTitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
    private List<ClubServiceTypeListBean.ClubServiceTypeBean> mServiceTypeList;
    private ClubServiceAdapter mClubServiceAdapter;
    private GetClubServiceParams mParams;
    private ClubServiceFilterAdapter mTypeFilterAdapter;
    private ClubServiceFilterAdapter mSortFilterAdapter;
    private Long typeListId;
    private Integer sortStatus;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_club_service;
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
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
    }

    public void changeParams() {
        mParams.setSortStatus(sortStatus);
        mParams.setServicelist(typeListId);
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
    }
}