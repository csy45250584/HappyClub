package com.haokuo.happyclub.activity;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.SignInBean;
import com.haokuo.happyclub.bean.list.SignInListBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wecoder.signcalendar.library.MonthSignData;
import cn.wecoder.signcalendar.library.SignCalendar;
import okhttp3.Call;

/**
 * Created by zjf on 2018/10/24.
 */
public class SignInActivity extends BaseActivity {
    //    @BindView(R.id.sign_calendar)
    SignCalendar mSignCalendar;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;
    @BindView(R.id.fl_calendar_container)
    FrameLayout mFlCalendarContainer;
    private ArrayList<MonthSignData> mMonthDatas;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void loadData() {
        HttpHelper.getInstance().getMonthSignIn(new EntityCallback<SignInListBean>() {
            @Override
            public void onFailure(Call call, String message) {
                ToastUtils.showShort("获取签到信息失败");
            }

            @Override
            public void onSuccess(Call call, SignInListBean result) {
                loadSignInData(result);
            }
        });
    }

    private void loadSignInData(SignInListBean result) {
        mSignCalendar = new SignCalendar(this, null);
        mFlCalendarContainer.addView(mSignCalendar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArrayList<Date> signDates = new ArrayList<>();
        List<SignInBean> data = result.getData();
        for (SignInBean signInBean : data) {
            Date date = TimeUtils.string2Date(signInBean.getSigninDate(), TimeUtils.CUSTOM_FORMAT);
            signDates.add(date);
        }
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.DAY_OF_MONTH, 23);
        signDates.add(instance.getTime());
        //存储多个月份的签到数据
        mMonthDatas = new ArrayList<>();
        //一个月份的签到数据
        MonthSignData monthData = new MonthSignData();
        Calendar calendar = Calendar.getInstance();
        monthData.setYear(calendar.get(Calendar.YEAR));
        monthData.setMonth(calendar.get(Calendar.MONTH));
        //一个月内的签到天数
        monthData.setSignDates(signDates);
        mMonthDatas.add(monthData);
        //给签到日历设置今天是哪一天
        mSignCalendar.setToday(calendar.getTime());
        //给签到日历设置数据
        mSignCalendar.setSignDatas(mMonthDatas);
    }

    private void signInToday() {
        MonthSignData monthSignData = mMonthDatas.get(0);
        ArrayList<Date> signDates = monthSignData.getSignDates();
        signDates.add(Calendar.getInstance().getTime());
        mSignCalendar.setSignDatas(mMonthDatas);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn_sign_in)
    public void onViewClicked() {
        if (mSignCalendar != null) {
            ArrayList<Date> signDates = mMonthDatas.get(0).getSignDates();
            if (signDates.contains(new Date())) {
                ToastUtils.showShort("今天已签到");
                return;
            }
            showLoading("签到中...");
            HttpHelper.getInstance().signIn(new NetworkCallback() {
                @Override
                public void onSuccess(Call call, String json) {
                    loadSuccess("签到成功,获得1积分", false);
                    signInToday();
                }

                @Override
                public void onFailure(Call call, String message) {
                    loadFailed("签到失败，" + message);
                }
            });
        }
    }
}
