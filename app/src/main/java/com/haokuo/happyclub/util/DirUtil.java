package com.haokuo.happyclub.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by zjf on 2018/9/5.
 */
public class DirUtil {
    public static final String EX_PARENT_DIR = "/HappyClub";
    public static final String EX_IMAGE_DIR = "/Image";
    public static final String EX_CACHE_DIR = "/Cache";
    private static File exParentDir = new File(Environment.getExternalStorageDirectory(), EX_PARENT_DIR);

    public static void createDir() {
        exParentDir.mkdir();
        new File(exParentDir, EX_IMAGE_DIR).mkdir();
        new File(exParentDir, EX_CACHE_DIR).mkdir();
    }

    public static String getImageDir() {
        return new File(exParentDir, EX_IMAGE_DIR).getAbsolutePath();
    }

    public static String getCacheDir() {
        return new File(exParentDir, EX_CACHE_DIR).getAbsolutePath();
    }
}
