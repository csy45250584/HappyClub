package com.haokuo.happyclub.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zjf on 2018/12/27.
 */
public class OkDownloadCallBack implements Callback {
    private static final String TAG = "OkDownloadCallBack";
    private DownloadCallback callback;
    private Handler mHandler;

    public OkDownloadCallBack(DownloadCallback callback) {
        this.callback = callback;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        Log.e(TAG, "okhttp failed, message = " + e.getMessage());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String message;
                if (e instanceof SocketTimeoutException) {
                    message = "连接超时";
                } else if (e instanceof ConnectException) {
                    message = "连接异常";
                } else {
                    message = e.getMessage();
                }
                callback.onFailure(call, message);
            }
        });
    }

    @Override
    public void onResponse(final Call call, Response response) {
        InputStream is = null;//输入流
        FileOutputStream fos = null;//输出流
        try {
            is = response.body().byteStream();//获取输入流
            final long total = response.body().contentLength();//获取文件大小
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStart(call, total);
                }
            });
            if (is != null) {
                Log.d(TAG, "onResponse: 不为空");
                File file = new File(callback.getFilePath());// 设置路径
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int ch = -1;
                long process = 0;
                while ((ch = is.read(buf)) != -1) {
                    fos.write(buf, 0, ch);
                    process += ch;
                    callback.onProgress(call, process,total);
                }
            }
            fos.flush();
            // 下载完成
            if (fos != null) {
                fos.close();
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onSuccess(call);
                }
            });
        } catch (final Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFailure(call, e.getMessage());
                }
            });
            Log.d(TAG, e.toString());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }
}
