package cn.finalteam.rxgalleryfinal.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 下午7:26
 */
public class EmptyViewUtils {

    public static void showLoading(ViewGroup emptyView) {
        if (emptyView == null) {
            return;
        }
        ProgressBar pbLoading = (ProgressBar) emptyView.getChildAt(0);
        pbLoading.setVisibility(View.VISIBLE);
        TextView tvEmptyMsg = (TextView) emptyView.getChildAt(1);
        tvEmptyMsg.setVisibility(View.GONE);
    }

    public static void showMessage(ViewGroup emptyView, String msg) {
        if (emptyView == null) {
            return;
        }
        ProgressBar pbLoading = (ProgressBar) emptyView.getChildAt(0);
        pbLoading.setVisibility(View.GONE);
        TextView tvEmptyMsg = (TextView) emptyView.getChildAt(1);
        tvEmptyMsg.setVisibility(View.VISIBLE);
        tvEmptyMsg.setText(msg);
    }
}
