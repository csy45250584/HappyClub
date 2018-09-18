package com.haokuo.happyclub.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.util.utilscode.TimeUtils;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;

/**
 * Created by zjf on 2018/9/14.
 */
public class TitleEditorView extends FrameLayout {
    private static final int TYPE_EDITOR = 0;
    private static final int TYPE_DATE_SELECTOR = 1;
    private static final int TYPE_SELECTOR = 2;
    private EditText mEtEditor;
    private TextView mTvDateSelector;
    private int mType;
    private Context mContext;
    private String mSelectText;

    public TitleEditorView(@NonNull Context context) {
        this(context, null);
    }

    public TitleEditorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //View
        View inflate = inflate(context, R.layout.view_title_editor, this);
        TextView tvTitle = inflate.findViewById(R.id.tv_title);
        mTvDateSelector = inflate.findViewById(R.id.tv_date_selector);
        mEtEditor = inflate.findViewById(R.id.et_editor);
        //TypedArray
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleEditorView);
        String titleText = typedArray.getString(R.styleable.TitleEditorView_titleText);
        String editorHint = typedArray.getString(R.styleable.TitleEditorView_editorHint);
        mSelectText = typedArray.getString(R.styleable.TitleEditorView_selectText);
        mType = typedArray.getInt(R.styleable.TitleEditorView_type, 0);
        typedArray.recycle();//释放
        //Set
        tvTitle.setText(titleText);
        switch (mType) {
            case TYPE_EDITOR:
                mEtEditor.setVisibility(VISIBLE);
                mTvDateSelector.setVisibility(GONE);
                mEtEditor.setHint(editorHint);
                break;
            case TYPE_DATE_SELECTOR:
                mEtEditor.setVisibility(GONE);
                mTvDateSelector.setVisibility(VISIBLE);
                mTvDateSelector.setText(mSelectText);
                mTvDateSelector.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDateDialog();
                    }
                });
                break;
            case TYPE_SELECTOR:
                mEtEditor.setVisibility(GONE);
                mTvDateSelector.setVisibility(VISIBLE);
                mTvDateSelector.setText(mSelectText);
                break;
        }
    }

    private void showDateDialog() {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(R.style.MyDatePicker) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                String date = dialog.getFormattedDate(TimeUtils.CUSTOM_FORMAT);
                mTvDateSelector.setText(date);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("确定")
                .negativeAction("取消");
        //        builder.build(mContext).show();
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(((BaseActivity) mContext).getSupportFragmentManager(), null);
    }

    public String getText() {
        switch (mType) {
            case TYPE_EDITOR:
                return mEtEditor.getEditableText().toString().trim();
            case TYPE_DATE_SELECTOR: {
                String text = mTvDateSelector.getText().toString().trim();
                if (text.equals(mSelectText)) {
                    return null;
                }
                return text;
            }
            case TYPE_SELECTOR: {
                String text = mTvDateSelector.getText().toString().trim();
                if (text.equals(mSelectText)) {
                    return null;
                }
                return text;
            }
        }
        return null;
    }

    public EditText getEditText() {
        return mEtEditor;
    }

    public void setText(String text) {
        switch (mType) {
            case TYPE_EDITOR:
                mEtEditor.setText(text);
                setEditorSelectionLast();
                break;
            case TYPE_DATE_SELECTOR:
                mTvDateSelector.setText(text);
            case TYPE_SELECTOR:
                mTvDateSelector.setText(text);
                break;
        }
    }

    public void setEditorSelectionLast() {
        mEtEditor.setSelection(mEtEditor.getEditableText().length());
    }


}
