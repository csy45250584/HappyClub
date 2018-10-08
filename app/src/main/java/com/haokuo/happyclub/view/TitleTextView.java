package com.haokuo.happyclub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;

/**
 * Created by zjf on 2018/9/14.
 */
public class TitleTextView extends FrameLayout {

    private TextView mTvText;

    public TitleTextView(@NonNull Context context) {
        this(context, null);
    }

    public TitleTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //View
        View inflate = inflate(context, R.layout.view_title_text, this);
        TextView tvTitle = inflate.findViewById(R.id.tv_title);
        mTvText = inflate.findViewById(R.id.tv_text);
        //TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleTextView);
        String titleText = typedArray.getString(R.styleable.TitleTextView_titleText);
        String contentText = typedArray.getString(R.styleable.TitleTextView_contentText);
        typedArray.recycle();//释放
        //Set
        tvTitle.setText(titleText);
        mTvText.setText(contentText);
    }

    public void setText(String text) {
        mTvText.setText(text);
    }

    public void setContentTextColor(int colorInt) {
        mTvText.setTextColor(colorInt);
    }
}
