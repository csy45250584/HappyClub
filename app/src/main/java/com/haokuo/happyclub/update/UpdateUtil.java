package com.haokuo.happyclub.update;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by zjf on 2018/12/14.
 */
public class UpdateUtil {
    private static UpdateUtil mInstance;
    private Handler mHandler;

    public static UpdateUtil getInstance() {
        if (mInstance == null) {
            synchronized (UpdateUtil.class) {
                if (mInstance == null) {
                    mInstance = new UpdateUtil();
                }
            }
        }
        return mInstance;
    }

    private UpdateUtil() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void checkUpdate(long timeOut, final Context context) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                //                //检测更新
                //                HttpHelper.getInstance().getNewVersion(new NetworkCallback() {
                //                    @Override
                //                    public void onSuccess(Call call, String json) {
                //                        UpdateAppResultBean resultBean = JSON.parseObject(json, UpdateAppResultBean.class);
                //                        if (resultBean.getVersionCode() > BuildConfig.VERSION_CODE) {
                //                            showUpdateDialog(resultBean,context);
                //                        }
                //                    }
                //
                //                    @Override
                //                    public void onFailure(Call call, String message) {
                //                        ToastUtils.showShort("获取更新信息失败," + message);
                //                    }
                //                });
                //            }
                //        }, timeOut);
            }
            //    private void showUpdateDialog(final UpdateAppResultBean updateAppResultBean, final Context context) {
            //        new AlertDialog.Builder(context)
            //                .setTitle("检测到新版本")
            //                .setMessage(updateAppResultBean.getUpdateContent())
            //                .setCancelable(false)
            //                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            //                    @Override
            //                    public void onClick(DialogInterface dialog, int which) {
            //                        //更新APP
            //                        String fileName = "WenYanOA_" + updateAppResultBean.getVersionName() + ".apk";
            //                        downFile(fileName, updateAppResultBean.getUrl(),context);
            //                    }
            //                })
            //                .setNegativeButton("取消", null)
            //                .create()
            //                .show();
            //
            //    }
            //下载apk操作
            //    public void downFile(String fileName, final String url, final Context context) {
            //        final ProgressDialog progressDialog = new ProgressDialog(context);//进度条，在下载的时候实时更新进度，提高用户友好度
            //        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //        progressDialog.setTitle("正在下载");
            //        progressDialog.setMessage("请稍候...");
            //        progressDialog.setProgress(0);
            //        progressDialog.show();
            //        final File apkFile = new File(DirUtil.getUpdateDir(), fileName);
            //        HttpHelper.getInstance().downloadFile(url, new DownloadCallback(apkFile.getAbsolutePath()) {
            //            @Override
            //            public void onStart(Call call, long fileLength) {
            //                progressDialog.setMax((int) fileLength);
            //            }
            //
            //            @Override
            //            public void onProgress(Call call, long progress) {
            //                progressDialog.setProgress((int) progress);
            //            }
            //
            //            @Override
            //            public void onSuccess(Call call) {
            //                progressDialog.dismiss();
            //                //下载成功，弹出安装应用窗口
            //                AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //                builder.setTitle("下载完成");
            //                builder.setMessage("是否安装？");
            //                builder.setCancelable(false);
            //                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //                    @Override
            //                    public void onClick(DialogInterface dialog, int which) {
            //                        Intent installAppIntent = IntentUtils.getInstallAppIntent(apkFile, "com.haokuo.wenyanoa.fileprovider");
            //                        context.startActivity(installAppIntent);
            //                    }
            //                });
            //                builder.setNegativeButton("取消", null);
            //                builder.create().show();
            //            }
            //
            //            @Override
            //            public void onFailure(Call call, String message) {
            //                //下载失败
            //                ToastUtils.showShort("下载APK文件失败，" + message);
            //                progressDialog.dismiss();
            //            }
            //        });
}
