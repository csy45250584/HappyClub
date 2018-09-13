package com.haokuo.happyclub.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.haokuo.happyclub.R;

/**
 * Created by zjf on 2018-07-10.
 */

public class SexRadioButton extends SettingItemView implements Checkable {

    private boolean checked;

    public SexRadioButton(Context context) {
        this(context, null);
    }

    public SexRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChecked(false);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        setRightIcon(checked ? R.drawable.d11 : R.drawable.d12);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {

    }
}
