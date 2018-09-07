package com.haokuo.midtitlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 标题置于中间
 * Created by zjf on 2018-07-10.
 */

public class MidTitleBar extends Toolbar {

    private static BarStyle sBarStyle;
    private Context mContext;
    private TextView mMidTitleView;
    private int mNaviIconId;
    private boolean mHasBackArrow;

    public MidTitleBar(Context context) {
        this(context, null);
    }

    public static BarStyle getBarStyle() {
        if (sBarStyle == null) {
            throw new RuntimeException("barStyle is uninitialized!");
        }
        return sBarStyle;
    }

    public MidTitleBar(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //TypedArray
        int backgroundColor = getBarStyle().getBackgroundColor();
        if (attrs != null) {
            backgroundColor = attrs.getAttributeIntValue(null, "background", getBarStyle().getBackgroundColor());
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MidTitleBar);
        CharSequence midTitle = typedArray.getText(R.styleable.MidTitleBar_midTitle);
        int titleColor = typedArray.getColor(R.styleable.MidTitleBar_titleColor, getBarStyle().getTitleColor());
        mHasBackArrow = typedArray.getBoolean(R.styleable.MidTitleBar_hasBackArrow, getBarStyle().isHasBackArrow());
        float titleSize = typedArray.getDimension(R.styleable.MidTitleBar_titleSize, getBarStyle().getTitleSize());
        mNaviIconId = typedArray.getResourceId(R.styleable.MidTitleBar_navigationIcon, getBarStyle().getNavigationIconId());
        typedArray.recycle();//释放
        setTitle("");
        setMidTitle((String) midTitle, titleSize, titleColor);
        //        if (hasBackArrow && mNaviIconId != 0 && context instanceof Activity) {
        //            addBackArrow((Activity) context, mNaviIconId);
        //        } else {
        //            setNavigationOnClickListener(null);
        //        }
        setBackgroundColor(backgroundColor);
    }

    public void setMidTitle(String midTitle, float titleSize, int titleColor) {
        if (mMidTitleView == null) {
            mMidTitleView = new TextView(mContext);
            mMidTitleView.setGravity(Gravity.CENTER);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            addView(mMidTitleView, params);
            Resources r = getResources();
            mMidTitleView.setTextSize(titleSize / r.getDisplayMetrics().density);
            mMidTitleView.setTextColor(titleColor);
        }
        mMidTitleView.setText(midTitle);
    }

    public void setMidTitle(String midTitle) {
        mMidTitleView.setText(midTitle);
    }

    public String getMidTitle() {
        return mMidTitleView.getText().toString();
    }

    public void addBackArrow(final Activity activity, int arrowId) {
        setNavigationIcon(arrowId);
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setOnTitleClickListener(OnClickListener onTitleClickListener) {
        mMidTitleView.setOnClickListener(onTitleClickListener);
    }

    public void addBackArrow(final Activity activity) {
        this.addBackArrow(activity, mNaviIconId);
    }

    public static void initStyle(BarStyle barStyle) {
        sBarStyle = barStyle;
    }

    public boolean hasBackArrow() {
        return mHasBackArrow;
    }
}
