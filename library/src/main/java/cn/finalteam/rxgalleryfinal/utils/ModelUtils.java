package cn.finalteam.rxgalleryfinal.utils;


import android.util.Log;

import cn.finalteam.rxgalleryfinal.BuildConfig;

/**
 * 模式
 * Created by KARL-dujinyang on 2017-03-17 02-24-08.
 */
public class ModelUtils {
    private static final String TAG = "Test";

    public static void logDebug() {
        Log.w(TAG, "BuildConfig.DEBUG:--" + BuildConfig.DEBUG + "--");
        if (BuildConfig.DEBUG)
            Logger.w("is debug mode");
        else
            Logger.w("not debug mode");
    }

    /**
     * 多层依赖时DEBUGCONFIG会出错，所以提供了内部接口更改
     *
     * @param f 是否打开
     */
    public static void setDebugModel(boolean f) {
        Logger.DEBUG = f;
    }

}
