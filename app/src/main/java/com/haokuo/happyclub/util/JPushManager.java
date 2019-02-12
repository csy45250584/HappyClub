package com.haokuo.happyclub.util;

import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zjf on 2018/12/17.
 */
public class JPushManager {
    private static JPushManager mInstance;

    public static JPushManager getInstance() {
        if (mInstance == null) {
            synchronized (JPushManager.class) {
                if (mInstance == null) {
                    mInstance = new JPushManager();
                }
            }
        }
        return mInstance;
    }

    private JPushManager() {
    }

    //设置极光推送
    public void init(Context context) {
        JPushInterface.init(context);
    }

    //设置tag标签
    public void setTags(Context context, String... tags) {
        HashSet<String> tagSet = new HashSet<>(Arrays.asList(tags));
        JPushInterface.setTags(context, 0, tagSet);
    }
}
