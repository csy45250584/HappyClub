package com.haokuo.happyclub.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.activity.CanteenActivity;
import com.haokuo.happyclub.activity.ClubServiceActivity;
import com.haokuo.happyclub.activity.ClubServiceDetailActivity;
import com.haokuo.happyclub.activity.ConvenienceActivity;
import com.haokuo.happyclub.activity.MoreActionActivity;
import com.haokuo.happyclub.activity.MyScoreActivity;
import com.haokuo.happyclub.activity.MyServeActivity;
import com.haokuo.happyclub.activity.NewsListActivity;
import com.haokuo.happyclub.activity.NursingActivity;
import com.haokuo.happyclub.activity.SchoolActivity;
import com.haokuo.happyclub.activity.SignInActivity;
import com.haokuo.happyclub.activity.VolunteerOrderActivity;
import com.haokuo.happyclub.activity.VolunteerServeActivity;
import com.haokuo.happyclub.adapter.ActionAdapter;
import com.haokuo.happyclub.adapter.ClubServiceAdapter;
import com.haokuo.happyclub.base.BaseLazyLoadFragment;
import com.haokuo.happyclub.bean.ActionBean;
import com.haokuo.happyclub.bean.NewsBean;
import com.haokuo.happyclub.bean.list.ClubServiceListBean;
import com.haokuo.happyclub.bean.list.NewsListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.bean.GetHotServiceParams;
import com.haokuo.happyclub.network.bean.GetNewsListParams;
import com.haokuo.happyclub.util.GlideImageLoader;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.RecyclerViewDivider;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/9/11.
 */
public class HomeFragment extends BaseLazyLoadFragment {
    @BindView(R.id.banner_home)
    Banner mBannerHome;
    @BindView(R.id.rv_action)
    RecyclerView mRvAction;
    @BindView(R.id.rv_volunteer_action)
    RecyclerView mRvVolunteerAction;
    @BindView(R.id.marquee_view_news)
    MarqueeView mMarqueeViewNews;
    @BindView(R.id.rv_hot_service)
    RecyclerView mRvHotService;
    private ActionAdapter mActionAdapter;
    private ActionAdapter mVolunteerActionAdapter;
    private ClubServiceAdapter mClubServiceAdapter;

    @Override
    protected int initContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        //banner设置
        mBannerHome.setImageLoader(new GlideImageLoader());
        ArrayList<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.tp1);
        bannerImages.add(R.drawable.tp2);
        bannerImages.add(R.drawable.tp3);
        bannerImages.add(R.drawable.tp4);
        mBannerHome.setImages(bannerImages);
        mBannerHome.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mBannerHome.start();
        //按钮设置
        mRvAction.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mActionAdapter = new ActionAdapter(R.layout.item_action);
        mRvAction.setAdapter(mActionAdapter);
        mRvVolunteerAction.setLayoutManager(new GridLayoutManager(mContext, 5, LinearLayoutManager.VERTICAL, false));
        mVolunteerActionAdapter = new ActionAdapter(R.layout.item_volunteer_action);
        mRvVolunteerAction.setAdapter(mVolunteerActionAdapter);
        initActionAdapter();
        initVolunteerActionAdapter();
        //热门服务设置
        mRvHotService.setLayoutManager(new LinearLayoutManager(mContext));
        int dividerHeight = (int) (getResources().getDimension(R.dimen.dp_1) + 0.5f);
        mRvHotService.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, dividerHeight, R.color.colorDivider,
                0, 0));
        mClubServiceAdapter = new ClubServiceAdapter(R.layout.item_hot_service);
        mRvHotService.setAdapter(mClubServiceAdapter);
    }

    @Override
    protected void loadData() {
        GetNewsListParams params = new GetNewsListParams(null, GetNewsListParams.STATUS_NEWS, null);
        HttpHelper.getInstance().getNewsList(params, new EntityCallback<NewsListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取头条信息失败");
            }

            @Override
            public void onSuccess(Call call, NewsListBean result) {
                List<NewsBean> data = result.getData();
                List<String> info = new ArrayList<>();
                for (NewsBean bean : data) {
                    info.add(bean.getTitle());
                }
                mMarqueeViewNews.startWithList(info);
            }
        });
        GetHotServiceParams getHotServiceParams = new GetHotServiceParams(5);
        HttpHelper.getInstance().getHotService(getHotServiceParams, new EntityCallback<ClubServiceListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取热门服务失败，" + message);
            }

            @Override
            public void onSuccess(Call call, ClubServiceListBean result) {
                mClubServiceAdapter.setNewData(result.getData());
            }
        });
    }

    public MarqueeView getMarqueeViewNews() {
        return mMarqueeViewNews;
    }

    private void initVolunteerActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("志愿者申请", R.drawable.zy1));
        actionBeans.add(new ActionBean("志愿工单", R.drawable.zy2, VolunteerOrderActivity.class));
        actionBeans.add(new ActionBean("我的服务", R.drawable.zy3, MyServeActivity.class));
        actionBeans.add(new ActionBean("服务评价", R.drawable.zy4));
        actionBeans.add(new ActionBean("我的积分", R.drawable.zy5, MyScoreActivity.class));
        mVolunteerActionAdapter.setNewData(actionBeans);
    }

    private void initActionAdapter() {
        ArrayList<ActionBean> actionBeans = new ArrayList<>();
        actionBeans.add(new ActionBean("签到", R.drawable.q1, SignInActivity.class));
        actionBeans.add(new ActionBean("会所服务", R.drawable.q2, ClubServiceActivity.class));
        actionBeans.add(new ActionBean("爱心便民", R.drawable.tbb1, ConvenienceActivity.class));
        actionBeans.add(new ActionBean("幸福食堂", R.drawable.q5, CanteenActivity.class));
        actionBeans.add(new ActionBean("幸福养老", R.drawable.q6,NursingActivity.class));
        actionBeans.add(new ActionBean("幸福学堂", R.drawable.q7,SchoolActivity.class));
        actionBeans.add(new ActionBean("志愿者服务", R.drawable.tbb2, VolunteerServeActivity.class));
        actionBeans.add(new ActionBean("更多", R.drawable.q8, MoreActionActivity.class));
        mActionAdapter.setNewData(actionBeans);
    }

    @Override
    protected void initListener() {
        mClubServiceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                long id = mClubServiceAdapter.getItem(position).getId();
                Intent intent = new Intent(mContext, ClubServiceDetailActivity.class);
                intent.putExtra(ClubServiceDetailActivity.EXTRA_SERVICE_ID, id);
                startActivity(intent);
            }
        });
        mActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mActionAdapter.getItem(position);
                if (item != null && item.getClz() != null) {
                    Intent intent = new Intent(mContext, item.getClz());
                    startActivity(intent);
                }
            }
        });
        mVolunteerActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean item = mVolunteerActionAdapter.getItem(position);
                if (item != null) {
                    if (item.getClz() != null) {
                        Intent intent = new Intent(mContext, item.getClz());
                        startActivity(intent);
                    } else {
                        switch (item.getTitle()) {
                            case "志愿者申请":

                                break;
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.ll_news_container)
    public void onViewClicked() {
        startActivity(new Intent(mContext, NewsListActivity.class));
    }
}
