package com.haokuo.happyclub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haokuo.happyclub.R;

/**
 * Created by zjf on 2018-07-09.
 */

public class SettingItemView extends FrameLayout {

    private String mLeftText;
    private TextView mTvRightText;
    private ImageView mIvRightIcon;
    private TextView mTvLeftText;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //View

        View inflate = inflate(context, R.layout.view_setting_item, this);
        mTvLeftText = inflate.findViewById(R.id.tv_left_text);
        mTvRightText = inflate.findViewById(R.id.tv_right_text);
        ImageView ivLeftIcon = inflate.findViewById(R.id.iv_left_icon);
        mIvRightIcon = inflate.findViewById(R.id.iv_right_icon);
        //TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        mLeftText = typedArray.getString(R.styleable.SettingItemView_leftText);
        float leftTextSize = typedArray.getDimension(R.styleable.SettingItemView_leftTextSize, getResources().getDimension(R.dimen.text_26px));
        int leftTextColor = typedArray.getColor(R.styleable.SettingItemView_leftTextColor, ContextCompat.getColor(context, R.color.colorText2));
        String rightText = typedArray.getString(R.styleable.SettingItemView_rightText);
        float rightTextSize = typedArray.getDimension(R.styleable.SettingItemView_rightTextSize, getResources().getDimension(R.dimen.text_26px));
        int rightTextColor = typedArray.getColor(R.styleable.SettingItemView_rightTextColor, ContextCompat.getColor(context, R.color.colorText2));
        Drawable leftIcon = typedArray.getDrawable(R.styleable.SettingItemView_leftIcon);
        Drawable rightIcon = typedArray.getDrawable(R.styleable.SettingItemView_rightIcon);
        typedArray.recycle();//释放
        //Set
        mTvLeftText.setText(mLeftText);
        mTvLeftText.setTextColor(leftTextColor);
        mTvLeftText.setTextSize(leftTextSize / getResources().getDisplayMetrics().density);
        mTvRightText.setText(rightText);
        mTvRightText.setTextColor(rightTextColor);
        mTvRightText.setTextSize(rightTextSize / getResources().getDisplayMetrics().density);
        if (leftIcon == null) {
            ivLeftIcon.setVisibility(GONE);
        } else {
            ivLeftIcon.setVisibility(VISIBLE);
            ivLeftIcon.setImageDrawable(leftIcon);
        }
        if (rightIcon == null) {
            mIvRightIcon.setVisibility(GONE);
        } else {
            mIvRightIcon.setVisibility(VISIBLE);
            mIvRightIcon.setImageDrawable(rightIcon);
        }
    }

    public String getLeftText() {
        if (TextUtils.isEmpty(mLeftText)) {
            throw new RuntimeException("the title is unassigned !");
        } else {
            return mTvLeftText.getText().toString();
        }
    }
    public void setLeftText(String content) {
        mTvLeftText.setText(content);
    }
    public void setRightText(String content) {
        mTvRightText.setText(content);
    }

    public void setRightIcon(int resId) {
        mIvRightIcon.setImageResource(resId);
    }

    public void setRightTextColor(int color) {
        mTvRightText.setTextColor(color);
    }
}
