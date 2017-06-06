package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/6/3 下午8:28
 */
public class CameraUtils {

    /**
     * 判断设备是否有摄像头
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }
}
