package cn.finalteam.rxgalleryfinal.utils;

import android.util.Log;

import cn.finalteam.rxgalleryfinal.BuildConfig;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/23 下午3:12
 */
public class Logger {

    public static final String TAG = "RxGalleryFinal";

    public static void d(String value){
        if(BuildConfig.DEBUG) {
            Log.d(TAG, value);
        }
    }

    public static void e(String value) {
        if(BuildConfig.DEBUG) {
            Log.e(TAG, value);
        }
    }

    public static void e(Exception value) {
        if(BuildConfig.DEBUG && value != null) {
            Log.e(TAG, value.getMessage());
        }
    }

    public static void i(String value){
        if(BuildConfig.DEBUG) {
            Log.i(TAG, value);
        }
    }

    public static void w(String value){
        if(BuildConfig.DEBUG) {
            Log.w(TAG, value);
        }
    }
}
