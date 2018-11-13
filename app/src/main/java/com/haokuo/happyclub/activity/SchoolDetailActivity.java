package com.haokuo.happyclub.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.bean.CourseBean;
import com.haokuo.happyclub.bean.CourseDetailBean;
import com.haokuo.happyclub.network.EntityCallback;
import com.haokuo.happyclub.network.HttpHelper;
import com.haokuo.happyclub.network.NetworkCallback;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.network.bean.CourseIdParams;
import com.haokuo.happyclub.util.ResUtils;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.rey.material.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by zjf on 2018/11/9.
 */
public class SchoolDetailActivity extends BaseActivity {
    public static final String EXTRA_COURSE_ID = "com.haokuo.happyclub.extra.EXTRA_COURSE_ID";
    @BindView(R.id.iv_course_pic)
    ImageView mIvCoursePic;
    @BindView(R.id.tv_course_name)
    TextView mTvCourseName;
    @BindView(R.id.tv_course_open_date)
    TextView mTvCourseOpenDate;
    @BindView(R.id.tv_course_time)
    TextView mTvCourseTime;
    @BindView(R.id.tv_course_type)
    TextView mTvCourseType;
    @BindView(R.id.tv_course_count)
    TextView mTvCourseCount;
    @BindView(R.id.tv_course_description)
    TextView mTvCourseDescription;
    @BindView(R.id.tv_course_address)
    TextView mTvCourseAddress;
    @BindView(R.id.btn_sign_in)
    Button mBtnSignIn;
    private CourseIdParams mParams;
    private CourseDetailBean mResult;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_school_detail;
    }

    @Override
    protected void initData() {
        long courseId = getIntent().getLongExtra(EXTRA_COURSE_ID, -1);
        mParams = new CourseIdParams(courseId);
    }

    @Override
    protected void loadData() {
        showLoading("课程加载中...");
        HttpHelper.getInstance().getCourseById(mParams, new EntityCallback<CourseDetailBean>() {
            @Override
            public void onFailure(Call call, String message) {
                loadFailed("加载课程失败，" + message);
                finish();
            }

            @Override
            public void onSuccess(Call call, CourseDetailBean result) {
                loadClose();
                mResult = result;
                CourseBean course = mResult.getCourse();
                //加载数据
                Glide.with(SchoolDetailActivity.this).load(UrlConfig.buildImageUrl(course.getCourseImage())).into(mIvCoursePic);
                mTvCourseName.setText(course.getCourseName());
                mTvCourseOpenDate.setText(course.getStartTime() + "至" + course.getEndTime());
                mTvCourseTime.setText(course.getSchoolTime());
                mTvCourseType.setText(course.getCourselistName());
                setCountInfo(course);
                mTvCourseDescription.setText(course.getDescription());
                mTvCourseAddress.setText(course.getPlace());
                if (result.isReserve()) {
                    mBtnSignIn.setText("取消预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                } else if (course.getCount().equals(course.getUserCount())) {
                    mBtnSignIn.setText("预约已满");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorDivider));
                } else {
                    mBtnSignIn.setText("立即预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                }
            }
        });
    }

    private void setCountInfo(CourseBean course) {
        String count = String.valueOf(course.getCount());
        String userCount = String.valueOf(course.getUserCount());
        SpannableString spannableString = new SpannableString(userCount + "/" + count);
        spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.colorPrimary)), 0, userCount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvCourseCount.setText(spannableString);
    }

    @OnClick(R.id.btn_sign_in)
    public void onViewClicked() {
        final CourseBean course = mResult.getCourse();
        if (mResult.isReserve()) {//取消预约
            showCancelReserveDialog();
        } else if (course.getCount().equals(course.getUserCount())) { //预约已满
            ToastUtils.showShort("预约人数已满，无法预约！");
        } else { //立即预约
            CourseIdParams params = new CourseIdParams(course.getId());
            showLoading("预约中...");
            HttpHelper.getInstance().reserveCourse(params, new NetworkCallback() {
                @Override
                public void onSuccess(Call call, String json) {
                    loadSuccess("预约成功", false);
                    mResult.setReserve(true);
                    course.setUserCount(course.getUserCount() + 1);
                    setCountInfo(course);
                    mBtnSignIn.setText("取消预约");
                    mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                    setResult(RESULT_OK);
                }

                @Override
                public void onFailure(Call call, String message) {
                    loadFailed("预约失败，" + message);
                }
            });
        }
    }



    private void showCancelReserveDialog() {
        new AlertDialog.Builder(this)
                .setMessage("是否取消预约该课程？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消预约
                        showLoading("取消预约中...");
                        HttpHelper.getInstance().cancelReserveCourse(mParams, new NetworkCallback() {
                            @Override
                            public void onSuccess(Call call, String json) {
                                loadSuccess("取消成功", false);
                                mResult.setReserve(false);
                                CourseBean course = mResult.getCourse();
                                course.setUserCount(course.getUserCount() - 1);
                                setCountInfo(course);
                                mBtnSignIn.setText("立即预约");
                                mBtnSignIn.setBackgroundColor(ResUtils.getColor(R.color.colorPrimary));
                                setResult(RESULT_OK);
                            }

                            @Override
                            public void onFailure(Call call, String message) {
                                loadFailed("取消失败，" + message);
                            }
                        });
                    }
                })
                .create()
                .show();
    }
}
