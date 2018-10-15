package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.CartFoodsAdapter;
import com.haokuo.happyclub.adapter.FoodListAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.AllFoodBean;
import com.haokuo.happyclub.bean.CartFoodBean;
import com.haokuo.happyclub.eventbus.OrderFoodEvent;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.util.utilscode.KeyboardUtils;
import com.haokuo.happyclub.util.utilscode.SizeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.happyclub.view.FloatingItemDecoration;
import com.haokuo.midtitlebar.MidTitleBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;

import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * Created by zjf on 2018/9/18.
 */
public class CanteenActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.tl_food_list)
    VerticalTabLayout mTlFoodList;
    @BindView(R.id.rv_food_list)
    RecyclerView mRvFoodList;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_total_score)
    TextView mTvTotalScore;
    @BindView(R.id.btn_settle_account)
    Button mBtnSettleAccount;
    @BindView(R.id.tv_cart_count)
    TextView mTvCartCount;
    @BindView(R.id.tv_slash)
    TextView mTvSlash;
    @BindView(R.id.fl_shopping_cart)
    FrameLayout mFlShoppingCart;
    private AllFoodBean mAllFoodBean;
    private FoodListAdapter mFoodListAdapter;
    private ArrayList<AllFoodBean.FoodBean> mFoodBeanList;
    private FloatingItemDecoration mFloatingItemDecoration;
    private HashMap<Integer, String> mKeys;
    private ArrayList<Integer> mTabPositions;
    private RecyclerView.SmoothScroller mSmoothScroller;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> mTabTitleList;
    private int mCartCount;
    private int mTotalScore;
    private BigDecimal mTotalPrice;
    private BottomSheetDialog mBottomSheetDialog;
    private RecyclerView mRvCartFoods;

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean getRegisterEventBus() {
        return true;
    }

    @Override
    protected int initContentLayout() {
        return R.layout.activity_canteen;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                KeyboardUtils.hideSoftInput(this, v);
                mSearchView.closeSearch();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_canteen, menu);
        MenuItem item = menu.getItem(0);
        mSearchView.setMenuItem(item);
        mSearchView.setCursorDrawable(R.drawable.search_bar_cursor);
        return true;
    }

    @Override
    protected void initData() {
        //设置RecyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRvFoodList.setLayoutManager(mLayoutManager);
        mFloatingItemDecoration = new FloatingItemDecoration(this, Color.BLUE, 1, 0);
        mFloatingItemDecoration.setmTitleHeight((int) (getResources().getDimension(R.dimen.dp_35) + 0.5f));
        mFloatingItemDecoration.setTitleTextSize((int) (getResources().getDimension(R.dimen.text_26px) + 0.5f));
        mFloatingItemDecoration.setTitleBgColor(ContextCompat.getColor(this, R.color.colorWhite));
        mFloatingItemDecoration.setTitleTextColor(ContextCompat.getColor(this, R.color.colorText1));
        mRvFoodList.addItemDecoration(mFloatingItemDecoration);
        mFoodListAdapter = new FoodListAdapter(R.layout.item_food);
        mRvFoodList.setAdapter(mFoodListAdapter);
        RecyclerView.ItemAnimator itemAnimator = mRvFoodList.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setChangeDuration(0);
        }
        mSmoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
    }

    @Override
    protected void loadData() {
        //从网络获取菜品列表
        HttpHelper.getInstance().getAllFoodList(new EntityCallback<AllFoodBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取菜品失败，" + message);
            }

            @Override
            public void onSuccess(Call call, AllFoodBean result) {
                mAllFoodBean = result;
                initTabAdapter();
                mKeys = new HashMap<>();
                List<AllFoodBean.FoodListBean> data = mAllFoodBean.getData();
                mFoodBeanList = new ArrayList<>();
                mTabPositions = new ArrayList<>();
                mTabTitleList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    AllFoodBean.FoodListBean foodListBean = data.get(i);
                    mTabPositions.add(mFoodBeanList.size());
                    mKeys.put(mFoodBeanList.size(), foodListBean.getFoodlist_name());
                    mFoodBeanList.addAll(foodListBean.getFoods());
                    mFloatingItemDecoration.setKeys(mKeys);
                    mTabTitleList.add(foodListBean.getFoodlist_name());
                }
                mTlFoodList.setTabSelected(0);
                getCartFromDb();
                mFoodListAdapter.setNewData(mFoodBeanList);
            }
        });
    }

    private void getCartFromDb() {
        //从数据库中取得购物车数据
        List<CartFoodBean> cartFoodList = LitePal.findAll(CartFoodBean.class);
        for (CartFoodBean cartFood : cartFoodList) {
            long id = cartFood.getFoodId();
            boolean isExist = false;
            for (AllFoodBean.FoodBean foodBean : mFoodBeanList) {
                if (foodBean.getId() == id) {
                    isExist = true;
                    foodBean.setBuyCount(cartFood.getCount());
                    break;
                }
            }
            if (!isExist) {
                cartFoodList.remove(cartFood);
                LitePal.delete(CartFoodBean.class, id);
            }
        }
        mCartCount = 0;
        mTotalPrice = BigDecimal.valueOf(0);
        mTotalScore = 0;
        for (CartFoodBean cartFoodBean : cartFoodList) {
            int count = cartFoodBean.getCount();
            mCartCount += count;
            mTotalPrice = mTotalPrice.add(BigDecimal.valueOf(cartFoodBean.getPrice()).multiply(BigDecimal.valueOf(count)));
            mTotalScore += cartFoodBean.getScore() * count;
        }
        setTotalInfoUi();
    }

    private void setTotalInfoUi() {
        if (mCartCount == 0) {
            mTvSlash.setVisibility(View.GONE);
            mTvTotalPrice.setVisibility(View.GONE);
            mTvTotalScore.setVisibility(View.GONE);
        } else {
            mTvSlash.setVisibility(View.VISIBLE);
            mTvTotalPrice.setVisibility(View.VISIBLE);
            mTvTotalScore.setVisibility(View.VISIBLE);
        }
        mTvCartCount.setText(String.valueOf(mCartCount));
        DecimalFormat decimalFormat = new DecimalFormat("¥0.00");
        mTvTotalPrice.setText(decimalFormat.format(mTotalPrice));
        mTvTotalScore.setText(String.valueOf(mTotalScore));
    }

    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.getLayoutManager().scrollToPosition(position);
            //            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                //                mRecyclerView.smoothScrollBy(0, top);
                mRecyclerView.scrollBy(0, top);
            }
        } else {
            mLayoutManager.scrollToPositionWithOffset(position, 0);
            mLayoutManager.setStackFromEnd(true);
            // 第三种可能:跳转位置在最后可见项之后
            //            mRecyclerView.getLayoutManager().scrollToPosition(position);
            //            mRecyclerView.scrollToPosition(position);
            ////            mRecyclerView.smoothScrollToPosition(position);
            //            mToPosition = position;
            //            mShouldScroll = true;
            //            smoothMoveToPosition(mRecyclerView,position);
        }
    }

    @Override
    protected void initListener() {
        mRvFoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
                String foodlistName = mFoodListAdapter.getItem(firstItem).getFoodlist_name();
                String title = mTlFoodList.getTabAt(mTlFoodList.getSelectedTabPosition()).getTitleView().getText().toString();
                if (foodlistName != null && !foodlistName.equals(title)) {
                    mTlFoodList.setTabSelected(mTabTitleList.indexOf(foodlistName), false);
                }
            }
        });
        mFoodListAdapter.setOnItemChildClickListener(this);
        //        mRvFoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {
        //            @Override
        //            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //                super.onScrollStateChanged(recyclerView, newState);
        //                Log.v("MY_CUSTOM_TAG", "CanteenActivity onScrollStateChanged()--> newState = " + newState + ",mShouldScroll = " + mShouldScroll);
        //                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
        //                    mShouldScroll = false;
        //                    //                    Log.v("MY_CUSTOM_TAG", "CanteenActivity onScrollStateChanged()-->");
        //                    smoothMoveToPosition(mRvFoodList, mToPosition);
        //                }
        //            }
        //        });
        mTlFoodList.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabView tab, int position) {
                Integer recyclerViewPosition = mTabPositions.get(position);
                //                mSmoothScroller.setTargetPosition(recyclerViewPosition);
                //                mLayoutManager.startSmoothScroll(mSmoothScroller);
                if (recyclerViewPosition != -1) {
                    mRvFoodList.scrollToPosition(recyclerViewPosition);
                    mLayoutManager.scrollToPositionWithOffset(recyclerViewPosition, 0);
                }

                //                mLayoutManager.scrollToPositionWithOffset(recyclerViewPosition, 0);
                //                mLayoutManager.setStackFromEnd(true);
                //                                smoothMoveToPosition(mRvFoodList, recyclerViewPosition);
                //                mTvFoodListTitle.setText(mAllFoodBean.getData().get(position).getFoodlist_name());
                //                mFoodListAdapter.setNewData(mAllFoodBean.getData().get(position).getFoods());
            }

            @Override
            public void onTabReselected(TabView tab, int position) {
                //                if (!init) {
                //                    mTvFoodListTitle.setText(mAllFoodBean.getData().get(position).getFoodlist_name());
                //                    mFoodListAdapter.setNewData(mAllFoodBean.getData().get(position).getFoods());
                //                    init = true;
                //                }
            }
        });
    }

    private void initTabAdapter() {
        mTlFoodList.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mAllFoodBean.getData().size();
            }

            @Override
            public TabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public TabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public TabView.TabTitle getTitle(int position) {

                return new ITabView.TabTitle.Builder()
                        .setTextSize(SizeUtils.px2sp(getResources().getDimension(R.dimen.text_26px)))
                        .setTextColor(R.color.colorPrimary, R.color.colorText2)
                        .setContent(mAllFoodBean.getData().get(position).getFoodlist_name())
                        .build();
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });
    }

    @OnClick({R.id.btn_settle_account, R.id.fl_shopping_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_settle_account:
                //下单
                if (mCartCount != 0) { //购物车数量不为0时展示购物车数据
                    startActivity(new Intent(CanteenActivity.this, FoodOrderActivity.class));
                } else {
                    ToastUtils.showShort("请添加先添加菜品到购物车");
                }
                break;
            case R.id.fl_shopping_cart:
                if (mCartCount != 0) { //购物车数量不为0时展示购物车数据
                    showBottomSheet();
                }
                break;
        }
    }

    //下单成功后
    @Subscribe
    public void onServeChangeEvent(OrderFoodEvent event) {
        clearCart();
    }

    private void showBottomSheet() {
        List<CartFoodBean> cartFoodList = LitePal.findAll(CartFoodBean.class);
        mBottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View v = LayoutInflater.from(this).inflate(R.layout.view_cart_foods, null);
        final TextView tvClearCart = v.findViewById(R.id.tv_clear_cart);
        tvClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
                mBottomSheetDialog.dismiss();
            }
        });
        mRvCartFoods = v.findViewById(R.id.rv_cart_foods);
        mRvCartFoods.setLayoutManager(new LinearLayoutManager(this)
                //        {
                //            @Override
                //            public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
                //                int childCount = getChildCount();
                //                if (childCount > 0) {
                //                    if (childCount > 4) {
                //                        View firstChildView = recycler.getViewForPosition(0);
                //                        measureChild(firstChildView, widthSpec, heightSpec);
                //                        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * 4);
                //                    } else {
                //                        View firstChildView = recycler.getViewForPosition(0);
                //                        measureChild(firstChildView, widthSpec, heightSpec);
                //                        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.getMeasuredHeight() * childCount);
                //                    }
                //                } else {
                //                    super.onMeasure(recycler, state, widthSpec, heightSpec);
                //                }
                //            }
                //        }
        );
        CartFoodsAdapter cartFoodsAdapter = new CartFoodsAdapter(R.layout.item_cart_food);
        //        ((DefaultItemAnimator) rvCartFoods.getItemAnimator()).setSupportsChangeAnimations(false);
        DefaultItemAnimator itemAnimator = (DefaultItemAnimator) mRvCartFoods.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }
        mRvCartFoods.setAdapter(cartFoodsAdapter);
        cartFoodsAdapter.setNewData(cartFoodList);
        cartFoodsAdapter.setOnItemChildClickListener(this);
        mBottomSheetDialog.contentView(v);
        mBottomSheetDialog.show();
    }

    private void clearCart() {
        //清空购物车
        LitePal.deleteAll(CartFoodBean.class);
        mCartCount = 0;
        mTotalPrice = BigDecimal.valueOf(0);
        mTotalScore = 0;
        setTotalInfoUi();
        mFoodListAdapter.deleteAllBuyCount();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof CartFoodsAdapter) { // 如果是购物车加减号
            CartFoodsAdapter cartFoodsAdapter = (CartFoodsAdapter) adapter;
            CartFoodBean cartItem = cartFoodsAdapter.getItem(position);
            if (cartItem == null) {
                return;
            }
            switch (view.getId()) {
                case R.id.iv_delete: {
                    cartItem.setCount(cartItem.getCount() - 1);
                    if (cartItem.getCount() == 0) {
                        if (cartFoodsAdapter.getData().size() == 0) {
                            mBottomSheetDialog.dismiss();
                        } else {
                            cartFoodsAdapter.remove(position);
                            //                            //当购物车减少一项时，RecyclerView自适应高度
                            //                            int childHeight = mRvCartFoods.getChildAt(0).getHeight();
                            //                            if (cartFoodsAdapter.getData().size() > 4) {
                            //                                ViewGroup.LayoutParams layoutParams = mRvCartFoods.getLayoutParams();
                            //                                layoutParams.height = 4 * childHeight;
                            //                                mRvCartFoods.setLayoutParams(layoutParams);
                            //                                mRvCartFoods.invalidate();
                            //                            }else {
                            //                                ViewGroup.LayoutParams layoutParams = mRvCartFoods.getLayoutParams();
                            //                                layoutParams.height = 4 * childHeight;
                            //                                mRvCartFoods.setLayoutParams(layoutParams);
                            //                                mRvCartFoods.invalidate();
                            //                            }
                        }
                    } else {
                        cartFoodsAdapter.notifyItemChanged(position);
                    }
                }
                break;
                case R.id.iv_add: {
                    cartItem.setCount(cartItem.getCount() + 1);
                    cartFoodsAdapter.notifyItemChanged(position);
                }
                break;
            }
            position = mFoodListAdapter.getItemPositionById(cartItem.getFoodId());
        }
        AllFoodBean.FoodBean menuItem = mFoodListAdapter.getItem(position);
        if (menuItem == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_delete: {
                menuItem.setBuyCount(menuItem.getBuyCount() - 1);
                mFoodListAdapter.notifyItemChanged(position);
                CartFoodBean cartFoodBean = new CartFoodBean(menuItem.getBuyCount(), menuItem.getFood_price(), menuItem.getFood_integral(), menuItem.getId(), menuItem.getFood_name(), menuItem.getFood_pictureurl());
                mCartCount -= 1;
                mTotalPrice = mTotalPrice.subtract(BigDecimal.valueOf(cartFoodBean.getPrice()));
                mTotalScore -= cartFoodBean.getScore();
                setTotalInfoUi();
                if (cartFoodBean.getCount() > 0) {
                    cartFoodBean.updateAll("foodId=?", String.valueOf(cartFoodBean.getFoodId()));
                } else {
                    LitePal.deleteAll(CartFoodBean.class, "foodId=?", String.valueOf(cartFoodBean.getFoodId()));
                }
            }
            break;
            case R.id.iv_add: {
                menuItem.setBuyCount(menuItem.getBuyCount() + 1);
                mFoodListAdapter.notifyItemChanged(position);
                CartFoodBean cartFoodBean = new CartFoodBean(menuItem.getBuyCount(), menuItem.getFood_price(), menuItem.getFood_integral(), menuItem.getId(), menuItem.getFood_name(), menuItem.getFood_pictureurl());
                mCartCount += 1;
                mTotalPrice = mTotalPrice.add(BigDecimal.valueOf(cartFoodBean.getPrice()));
                mTotalScore += cartFoodBean.getScore();
                setTotalInfoUi();
                if (cartFoodBean.getCount() > 1) {
                    cartFoodBean.updateAll("foodId=?", String.valueOf(cartFoodBean.getFoodId()));
                } else {
                    cartFoodBean.save();
                }
            }
            break;
        }
    }
}