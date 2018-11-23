package com.haokuo.happyclub.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.adapter.MainFragmentPagerAdapter;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.eventbus.LogoutEvent;
import com.haokuo.happyclub.fragment.ActivityFragment;
import com.haokuo.happyclub.fragment.HomeFragment;
import com.haokuo.happyclub.fragment.MeFragment;
import com.haokuo.happyclub.fragment.OrderFragment;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.bean.UpdateActivitySignParams;
import com.haokuo.happyclub.qrcode.OperationBean;
import com.haokuo.happyclub.qrcode.VolunteerActivityBean;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private static final int DEFAULT_TAB_POSITION = 0;
    private static final int REQUEST_CODE_SCAN = 1;
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    private OrderFragment mOrderFragment;
    private HomeFragment mHomeFragment;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initBottomNaviBar();
        ArrayList<Fragment> fragments = new ArrayList<>();
        //        fragments.add(new IMFragment());
        mHomeFragment = new HomeFragment();
        fragments.add(mHomeFragment);
        fragments.add(new ActivityFragment());
        //        fragments.add(new NearbyFragment());
        mOrderFragment = new OrderFragment();
        fragments.add(mOrderFragment);
        fragments.add(new MeFragment());
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size()); //设置缓存fragment数量，防止fragment生命周期多次调用
        mViewPager.setCurrentItem(DEFAULT_TAB_POSITION); //设置初始化的位置
    }

    @Override
    protected boolean getRegisterEventBus() {
        return true;
    }

    private void initBottomNaviBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.colorPrimary)//选中颜色 图标和文字
                .setInActiveColor(R.color.colorText3)//默认未选择颜色
                .setBarBackgroundColor(R.color.colorWhite);//默认背景色
        mBottomNavigationBar
                //                .addItem(new BottomNavigationItem(R.drawable.news, "消息"))
                .addItem(new BottomNavigationItem(R.drawable.s1a, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.s2a, "活动"))
                //                .addItem(new BottomNavigationItem(R.drawable.s3a, "附近"))
                .addItem(new BottomNavigationItem(R.drawable.s4a, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.s5a, "我的"))
                .setFirstSelectedPosition(DEFAULT_TAB_POSITION)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成
    }

    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_scan_qrcode:
                //开启扫描二维码
                MainActivityPermissionsDispatcher.startScanQrcodeWithPermissionCheck(this);
                break;
        }
        return true;
    }

    //    private void startScanQrcode() {
    //        startActivity(new Intent(MainActivity.this, ScanQrcodeActivity.class));
    //    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mOrderFragment.getUserVisibleHint() && mOrderFragment.isMenuShowing()) {
            mOrderFragment.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
                switch (position) {
                    case 0:
                        mMidTitleBar.setMidTitle("首页");
                        break;
                    //                    case 1:
                    //                        mMidTitleBar.setMidTitle("活动");
                    //                        mMidTitleBar.getMenu().clear();
                    //                        break;
                    case 1:
                        mMidTitleBar.setMidTitle("活动");
                        break;
                    case 2:
                        mMidTitleBar.setMidTitle("订单");
                        break;
                    case 3:
                        mMidTitleBar.setMidTitle("我的");
                        //                        mMidTitleBar.getMenu().add(0, R.id.item_edit_info, 0, null).setIcon(R.drawable.xx1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mHomeFragment != null && mHomeFragment.getMarqueeViewNews() != null) {
            mHomeFragment.getMarqueeViewNews().startFlipping();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHomeFragment != null && mHomeFragment.getMarqueeViewNews() != null) {
            mHomeFragment.getMarqueeViewNews().stopFlipping();
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void startScanQrcode() {
        //打开扫描界面扫描条形码或二维码
        Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void scanQrcodeDenied() {
        ToastUtils.showShort("您拒绝了照相机权限，无法打开摄像头！");
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void scanQrcodeNeverAskAgain() {
        new AlertDialog.Builder(this)
                .setTitle("权限提示")
                .setMessage("无法获取照相机权限，是否跳转到手机设置，以允许应用获取照相机权限？")
                .setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtils.showShort("您拒绝了照相机权限，无法打开摄像头！");
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    showLoading("");
                    OperationBean operationBean = JSON.parseObject(scanResult, OperationBean.class);
                    if (operationBean == null) {
                        loadFailed("非该应用二维码，请重新扫描！");
                    } else { //APP规定协议内的操作
                        switch (operationBean.getOperation()) {
                            case OperationBean.OPERATION_VOLUNTEER_ACTIVITY:
                                VolunteerActivityBean volunteerActivityBean = JSON.parseObject(scanResult, VolunteerActivityBean.class);
                                if (volunteerActivityBean == null) {
                                    loadFailed("二维码信息异常，请重新扫描！");
                                } else {
                                    final String typeString = volunteerActivityBean.getSignType() == VolunteerActivityBean.TYPE_SIGN_UP ? "签到" : "签退";
                                    UpdateActivitySignParams params = new UpdateActivitySignParams(volunteerActivityBean.getActivityId(), volunteerActivityBean.getSignType());
                                    HttpHelper.getInstance().updateActivitySign(params, new NetworkCallback() {
                                        @Override
                                        public void onSuccess(Call call, String json) {
                                            loadSuccess(typeString + "成功");
                                        }

                                        @Override
                                        public void onFailure(Call call, String message) {
                                            loadFailed(typeString + "失败，" + message);
                                        }
                                    });
                                }
                                break;
                        }
                    }
                }
                break;
        }
    }
}
