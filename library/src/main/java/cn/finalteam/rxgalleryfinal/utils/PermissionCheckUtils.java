package cn.finalteam.rxgalleryfinal.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.List;

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
    public static void checkPermission(Activity activity, String permission, OnPermissionCallback callback) {


        XXPermissions.with(activity)
                // 申请单个权限
                .permission(permission)
                // 申请多个权限
                //.permission(Permission.Group.CALENDAR)
                // 申请安装包权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 申请悬浮窗权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW)
                // 申请通知栏权限
                //.permission(Permission.NOTIFICATION_SERVICE)
                // 申请系统设置权限
                //.permission(Permission.WRITE_SETTINGS)
                // 设置权限请求拦截器
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制
                //.unchecked()
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            //Toast.makeText(activity, "获取权限成功", Toast.LENGTH_SHORT).show();
                        } else {
                            //toast("获取部分权限成功，但部分权限未正常授予");
                            //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
                        }
                        callback.onGranted(permissions,all);

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            //toast("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(activity, permissions);
                        } else {
                            //toast("获取录音和日历权限失败");
                            callback.onDenied(permissions,never);
                        }

                    }
                });

    }


    /**
     * 检查是否对sd卡读取授权
     */
    @TargetApi(16)
    public static void checkReadExternalPermission(Activity activity, String permissionDesc, int requestCode, OnPermissionCallback callback) {
         checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE,callback);
    }


    /**
     * 检查是否对sd卡读取授权
     */
    @TargetApi(16)
    public static void checkWriteExternalPermission(Activity activity, String permissionDesc, int requestCode, OnPermissionCallback callback) {
         checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, callback);
    }

    /**
     * 检查是否对相机读取授权
     */
    @TargetApi(16)
    public static void checkCameraPermission(Activity activity, String permissionDesc, int requestCode, OnPermissionCallback callback) {
         checkPermission(activity, Manifest.permission.CAMERA, callback);
    }

}
