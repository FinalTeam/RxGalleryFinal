package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/8/8 下午11:05
 */
public class DeviceUtils {

    public static DisplayMetrics getScreenSize(Context context) {
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        context.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }
}
