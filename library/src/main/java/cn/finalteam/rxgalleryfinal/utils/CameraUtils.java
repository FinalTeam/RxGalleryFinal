package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/6/3 下午8:28
 */
public class CameraUtils {

    /**
     * 判断设备是否有摄像头
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return false;
        }

        return true;
    }
}
