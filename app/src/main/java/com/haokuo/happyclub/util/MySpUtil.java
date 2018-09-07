package com.haokuo.happyclub.util;

import com.haokuo.happyclub.consts.SpConsts;
import com.haokuo.happyclub.util.utilscode.SPUtils;

/**
 * Created by zjf on 2018/9/7.
 */
public class MySpUtil {
    private static MySpUtil mInstance;
    private final SPUtils mPersonInfoSp;

    public static MySpUtil getInstance() {
        if (mInstance == null) {
            synchronized (MySpUtil.class) {
                if (mInstance == null) {
                    mInstance = new MySpUtil();
                }
            }
        }
        return mInstance;
    }

    private MySpUtil() {
        mPersonInfoSp = SPUtils.getInstance(SpConsts.FILE_PERSONAL_INFORMATION);
    }

    public long getUserId() {
        return mPersonInfoSp.getLong(SpConsts.KEY_USER_ID, -1);
    }

    public String getToken() {
        return mPersonInfoSp.getString(SpConsts.KEY_TOKEN);
    }
}
