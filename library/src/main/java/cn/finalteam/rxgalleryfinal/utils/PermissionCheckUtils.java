package cn.finalteam.rxgalleryfinal.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Desction:权限检查工具
 * Author:pengjianbo  Dujinyang
 * Author:KARL-dujinyang
 * Date:16/6/1 下午7:40
 */
public class PermissionCheckUtils {

    /**
     * 数组
     */
    public static boolean checkPermission(Activity activity, String permission, String permissionDesc, int requestCode) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                Logger.i("ContextCompat.checkSelfPermission(activity, permission):" + ContextCompat.checkSelfPermission(activity, permission));
                Logger.i("PackageManager.PERMISSION_GRANTED:" + PackageManager.PERMISSION_GRANTED);
                Logger.i("permission:" + permission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
                    alertBuilder.setCancelable(false);
                    alertBuilder.setTitle("授权对话框");
                    alertBuilder.setMessage(permissionDesc);
                    alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode));
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * 检查是否对sd卡读取授权
     */
    @TargetApi(16)
    public static boolean checkReadExternalPermission(Activity activity, String permissionDesc, int requestCode) {
        return checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, permissionDesc, requestCode);
    }


    /**
     * 检查是否对sd卡读取授权
     */
    @TargetApi(16)
    public static boolean checkWriteExternalPermission(Activity activity, String permissionDesc, int requestCode) {
        return checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionDesc, requestCode);
    }

    /**
     * 检查是否对相机读取授权
     */
    @TargetApi(16)
    public static boolean checkCameraPermission(Activity activity, String permissionDesc, int requestCode) {
        return checkPermission(activity, Manifest.permission.CAMERA, permissionDesc, requestCode);
    }

}
