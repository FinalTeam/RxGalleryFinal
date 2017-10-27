package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/14 下午7:10
 */
public class ThemeUtils {
    public static int resolveColor(Context context, @AttrRes int attr) {
        return resolveColor(context, attr, 0);
    }

    public static int resolveColor(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        int color = 0;
        if (fallback != 0) {
            color = ContextCompat.getColor(context, fallback);
        }
        try {
            return a.getColor(0, color);
        } finally {
            a.recycle();
        }
    }

    public static float resolveDimen(Context context, @AttrRes int attr) {
        return resolveDimen(context, attr, 0);
    }

    public static float resolveDimen(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        float size = 0;
        if (fallback != 0) {
            size = context.getResources().getDimension(fallback);
        }
        try {
            return a.getDimension(0, size);
        } finally {
            a.recycle();
        }
    }

    public static String resolveString(Context context, @AttrRes int attr) {
        return resolveString(context, attr, 0);
    }

    public static String resolveString(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        String value;
        try {
            String s = a.getString(0);
            if (TextUtils.isEmpty(s)) {
                s = context.getString(fallback);
            }
            value = s;
        } finally {
            a.recycle();
        }

        return value;
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr) {
        return resolveBoolean(context, attr, 0);
    }

    public static boolean resolveBoolean(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        boolean defValue = false;
        if (fallback != 0) {
            defValue = context.getResources().getBoolean(fallback);
        }
        try {
            return a.getBoolean(0, defValue);
        } finally {
            a.recycle();
        }
    }

    public static int resolveInteger(Context context, @AttrRes int attr) {
        return resolveInteger(context, attr, 0);
    }

    public static int resolveInteger(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        int defValue = 0;
        if (fallback != 0) {
            defValue = context.getResources().getInteger(fallback);
        }
        try {
            return a.getInteger(0, defValue);
        } finally {
            a.recycle();
        }
    }

    public static int resolveDrawableRes(Context context, @AttrRes int attr) {
        return resolveDrawableRes(context, attr, 0);
    }

    public static int resolveDrawableRes(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getResourceId(0, fallback);
        } finally {
            a.recycle();
        }
    }

    public static Drawable resolveDrawable(Context context, @AttrRes int attr) {
        return resolveDrawable(context, attr, 0);
    }

    public static Drawable resolveDrawable(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        Drawable drawable = null;
        if (fallback != 0) {
            drawable = ContextCompat.getDrawable(context, fallback).mutate();
        }
        try {
            Drawable d = a.getDrawable(0);
            if (d != null) {
                drawable = d;
            }
        } finally {
            a.recycle();
        }

        return drawable;
    }

    public static float applyDimensionDp(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * Sets status-bar color for L devices.
     *
     * @param color - status-bar color
     */
    public static void setStatusBarColor(@ColorInt int color, Window window) {
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(color);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(true);
                }
                View statusBarView = new View(window.getContext());
                int statusBarHeight = getStatusBarHeight(window.getContext());
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
                params.gravity = Gravity.TOP;
                statusBarView.setLayoutParams(params);
                statusBarView.setBackgroundColor(color);
                mContentView.addView(statusBarView);
            }
        }
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
