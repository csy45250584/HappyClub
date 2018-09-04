package com.haokuo.happyclub.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.haokuo.wenyanoa.R;

/**
 * Created by zjf on 2018/8/22.
 */
public class ImageLoadUtil {

    private static ImageLoadUtil sInstance;

    public static ImageLoadUtil getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoadUtil.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoadUtil();
                }
            }
        }
        return sInstance;
    }

    public ImageLoadUtil() {

    }

    public static RequestOptions sManAvatarOptions = new RequestOptions()
            .placeholder(R.drawable.man)
            .error(R.drawable.man);
    public static RequestOptions sWomanAvatarOptions = new RequestOptions()
            .placeholder(R.drawable.woman)
            .error(R.drawable.woman);

    public void loadAvatar(Context context, String url, ImageView iv, String sex) {
        Glide.with(context).load(url).apply("女".equals(sex) ? sWomanAvatarOptions : sManAvatarOptions).into(iv);
    }
}
