package com.haokuo.happyclub.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.haokuo.happyclub.util.utilscode.AppUtils;

/**
 * Created by zjf on 2018/12/29.
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName1 = intent.getDataString();
        Log.v("MY_CUSTOM_TAG", "UpdateReceiver onReceive()-->" + packageName1);
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            Log.v("MY_CUSTOM_TAG", "UpdateReceiver onReceive()-->" + "监听到系统广播添加");
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            Log.v("MY_CUSTOM_TAG", "UpdateReceiver onReceive()-->" + "监听到系统广播移除");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.v("MY_CUSTOM_TAG", "UpdateReceiver onReceive()-->" + "监听到系统广播替换"+packageName);
            if (packageName.equals(AppUtils.getAppPackageName())) {
            Log.v("MY_CUSTOM_TAG", "UpdateReceiver onReceive()-->" + "监听到自己系统广播替换");
            }
        }
    }
}
