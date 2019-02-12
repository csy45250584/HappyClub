package com.haokuo.happyclub.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.update.OKHttpUpdateHttpService;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.JPushManager;
import com.haokuo.happyclub.util.MySpUtil;
import com.haokuo.happyclub.util.utilscode.Utils;
import com.haokuo.midtitlebar.BarStyle;
import com.haokuo.midtitlebar.MidTitleBar;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.litepal.LitePal;

/**
 * Created by zjf on 2018-07-16.
 */

public class HappyClubApplication extends Application {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new MaterialHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化工具类
        Utils.init(getApplicationContext());
        //设置极光推送
        JPushManager.getInstance().init(this);
        //设置tag标签
        long userId = MySpUtil.getInstance().getUserId();
        if (userId != -1) {
            JPushManager.getInstance().setTags(this, String.valueOf(userId));
        }
        //创建文件夹
        createDirs();
        //初始化全局Loading样式
        initLoadingStyle();
        //初始化ToolBar样式
        initBarStyle();
        //数据库初始化
        LitePal.initialize(this);
        initUpdate();
    }

    private void initUpdate() {
        //
        XUpdate.get()
                .debug(true) //开启debug模式，可用于问题的排查
                .isWifiOnly(true)     //默认设置只在wifi下检查版本更新
                .isGet(false)          //默认设置使用get请求检查版本
                .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("type", 1)
                .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        Log.e("XUpdate", "onFailure : " + "错误代码："+error.getCode()+"，错误原因："+error.getDetailMsg());
                    }
                })
                .setIUpdateHttpService(new OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(this);   //这个必须初始化
    }

    private void initBarStyle() {
        Resources resources = getResources();
        BarStyle barStyle = new BarStyle.Builder()
                .setBackgroundColor(resources.getColor(R.color.colorPrimary))
                .setTitleColor(resources.getColor(R.color.colorWhite))
                .setTitleSize(resources.getDimension(R.dimen.sp_19))
                .setHasBackArrow(true)
                .setNavigationIconId(R.drawable.t1)
                .build();
        MidTitleBar.initStyle(barStyle);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private void initLoadingStyle() {
        StyleManager styleManager = new StyleManager();
        styleManager.loadText("提交中...").successText("提交成功").failedText("提交失败")
                .loadingColor(getResources().getColor(R.color.colorPrimary))
                .speed(LoadingDialog.Speed.SPEED_FAST).showSuccessTime(500).showFailedTime(800).finishSuccess(true);
        LoadingDialog.initStyle(styleManager);
    }

    private void createDirs() {
        DirUtil.createDir();
    }
}
