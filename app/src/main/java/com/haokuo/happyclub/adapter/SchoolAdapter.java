package com.haokuo.happyclub.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.bean.CourseBean;
import com.haokuo.happyclub.network.UrlConfig;
import com.haokuo.happyclub.util.ResUtils;

/**
 * Created by Naix on 2017/8/7 17:29.
 */
public class SchoolAdapter extends BaseQuickAdapter<CourseBean, BaseViewHolder> {

    public SchoolAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CourseBean item) {
        ImageView ivSchoolPic = helper.getView(R.id.iv_school_pic);
        Glide.with(mContext).load(UrlConfig.buildImageUrl(item.getCourseImage())).into(ivSchoolPic);
        helper.setText(R.id.tv_course_name, item.getCourseName());
        helper.setText(R.id.tv_course_description, item.getDescription());
        helper.setText(R.id.tv_course_time, item.getStartTime() + "开课 " + item.getSchoolTime());
        //        helper.setText(R.id.tv_score, String.valueOf(item.getCredit()));
        TextView tvSignUpCount = helper.getView(R.id.tv_sign_up_count);
        String count = String.valueOf(item.getCount());
        String userCount = String.valueOf(item.getUserCount());
        SpannableString spannableString = new SpannableString(userCount + "/" + count);
        spannableString.setSpan(new ForegroundColorSpan(ResUtils.getColor(R.color.colorPrimary)), 0, userCount.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvSignUpCount.setText(spannableString);
//        TextView tvSignUp = helper.getView(R.id.tv_sign_up);
//        GradientDrawable tvSignUpBackground = (GradientDrawable) tvSignUp.getBackground();
//        if (item.getCount().equals(item.getUserCount())) {
//            tvSignUpBackground.setColor(ResUtils.getColor(R.color.colorDivider));
//            tvSignUp.setText("报名已满");
//        } else {
//            tvSignUpBackground.setColor(ResUtils.getColor(R.color.colorOrange));
//            tvSignUp.setText("我要报名");
//        }
//        helper.addOnClickListener(R.id.tv_sign_up);
    }
}
