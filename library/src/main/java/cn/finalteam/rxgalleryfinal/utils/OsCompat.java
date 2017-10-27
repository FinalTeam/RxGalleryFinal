package cn.finalteam.rxgalleryfinal.utils;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/20 下午4:23
 */
public class OsCompat {

    public static void setBackgroundDrawableCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
    }

}
