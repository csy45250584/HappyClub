package com.haokuo.happyclub.util;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 使用弱引用来防止Handler内存泄漏.<br>
 * <p></p>
 * Created by 蔡书宜 on 2017/6/28.
 */
public class SafeHandler<T> extends Handler {
    private final WeakReference<T> mReference;
    private final WeakReference<Callback> mCallback;

    public SafeHandler(T reference, Callback callback) {
        if (reference == null) {
            throw new IllegalArgumentException("reference is null!");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback is null!");
        }
        mReference = new WeakReference<>(reference);
        mCallback = new WeakReference<>(callback);
    }
    @Override
    public void handleMessage(Message msg) {
        if (mReference != null && mReference.get() != null && mCallback != null && mCallback.get() != null) {
            mCallback.get().handleMessage(msg);
        }
    }
}

//    private Handler.Callback mCallback = new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//              //内容
//            return true;
//        }
//    };
//    private SafeHandler mHandler = new SafeHandler<Activity or Fragment>(this, mCallback);
