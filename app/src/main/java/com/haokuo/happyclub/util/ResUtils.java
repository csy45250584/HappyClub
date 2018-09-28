package com.haokuo.happyclub.util;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import com.haokuo.happyclub.util.utilscode.Utils;

import java.io.InputStream;

/**
 * Created by 蔡书宜 on 2017/5/26.
 */

public class ResUtils {
    public static Resources getResources() {
        return Utils.getContext().getResources();
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(Utils.getContext(), colorId);
    }

    public static int getDimens(int dimensId) {
        return getResources().getDimensionPixelSize(dimensId);
    }

    public static String getString(int stringId) {
        return getResources().getString(stringId);
    }

    public static String getString(int stringId, Object... formatArgs) {
        return getResources().getString(stringId, formatArgs);
    }

    public static String[] getStringArray(int stringArrayId) {
        return getResources().getStringArray(stringArrayId);
    }

    public static InputStream getRawInputStream(int rawId) {
        return getResources().openRawResource(rawId);
    }
}
