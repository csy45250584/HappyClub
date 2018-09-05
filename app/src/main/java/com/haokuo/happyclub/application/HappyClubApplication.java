package com.haokuo.happyclub.application;

import android.app.Application;
import android.content.Context;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.util.DirUtil;
import com.haokuo.happyclub.util.utilscode.Utils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

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
        //创建文件夹
        createDirs();
        //初始化全局Loading样式
        //        initLoadingStyle();
        //初始化ToolBar样式
        //        initBarStyle();
        //数据库初始化
        //        LitePal.initialize(this);
    }

    //    private void initBarStyle() {
    //        Resources resources = getResources();
    //        BarStyle barStyle = new BarStyle.Builder()
    //                .setBackgroundColor(resources.getColor(R.color.colorPrimary))
    //                .setTitleColor(resources.getColor(R.color.colorBarTitle))
    //                .setTitleSize(resources.getDimension(R.dimen.sp_19))
    //                .setHasBackArrow(true)
    //                .setNavigationIconId(R.drawable.fanhui1)
    //                .build();
    //        MidTitleBar.initStyle(barStyle);
    //    }
    //
    //    private void initLoadingStyle() {
    //        StyleManager styleManager = new StyleManager();
    //        styleManager.loadText("提交中...").successText("提交成功").failedText("提交失败")
    //                .loadingColor(getResources().getColor(R.color.colorPrimary))
    //                .speed(LoadingDialog.Speed.SPEED_FAST).showSuccessTime(500).showFailedTime(800).finishSuccess(true);
    //        LoadingDialog.initStyle(styleManager);
    //    }

    private void createDirs() {
        DirUtil.createDir();
    }
}
