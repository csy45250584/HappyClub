package com.haokuo.happyclub.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.haokuo.happyclub.R;
import com.haokuo.happyclub.base.BaseActivity;
import com.haokuo.happyclub.util.utilscode.ToastUtils;
import com.haokuo.midtitlebar.MidTitleBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zjf on 2018/9/26.
 */
public class FoodOrderNoteActivity extends BaseActivity {
    public static final String EXTRA_NOTE = "com.haokuo.happyclub.extra.EXTRA_NOTE";
    @BindView(R.id.mid_title_bar)
    MidTitleBar mMidTitleBar;
    @BindView(R.id.et_note)
    EditText mEtNote;
    @BindView(R.id.lv_note)
    LabelsView mLvNote;
    @BindView(R.id.tv_word_count)
    TextView mTvWordCount;

    @Override
    protected int initContentLayout() {
        return R.layout.activity_food_order_note;
    }

    @Override
    protected void initData() {
        //获得
        ArrayList<String> labels = new ArrayList<>();
        labels.add("不要葱");
        labels.add("不要蒜");
        labels.add("不要醋");
        labels.add("少放辣");
        labels.add("不吃香菜");
        labels.add("饮料要冰的");
        mLvNote.setLabels(labels);
        //从Intent中取得note数据
        String note = getIntent().getStringExtra(EXTRA_NOTE);
        if (note != null) {
            //将note设置到editText中
            mEtNote.setText(note);
            mEtNote.setSelection(note.length());
            //获得应该选中的label位置
            ArrayList<Integer> selectPositions = new ArrayList<>();
            String[] split = note.split(" ");
            for (String temp : split) {
                int index = labels.indexOf(temp);
                if (index != -1) {
                    selectPositions.add(index);
                }
            }
            //将label设置为选中状态
            if (selectPositions.size() > 0) {
                mLvNote.setSelects(selectPositions);
            }
        }
    }

    @Override
    protected void initListener() {
        mMidTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.item_complete) {
                    //点击完成
                    String note = mEtNote.getEditableText().toString().trim();
                    if (note.length() == 0) {
                        ToastUtils.showShort("请填写备注信息");
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_NOTE, note);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                return true;
            }
        });
        mLvNote.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                if (isSelect) {
                    String temp = mEtNote.getEditableText().toString() + " " + label.getText();
                    if (temp.length() > 70) {
                        ToastUtils.showShort("最多70个字哦~");
                        return;
                    }
                    mEtNote.setText(temp);
                    mEtNote.setSelection(temp.length());
                }
            }
        });
        mEtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length == 70) {
                    ToastUtils.showShort("最多70个字哦~");
                }
                //监听字数变化
                mTvWordCount.setText(String.format("%d/70", length));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }
}
