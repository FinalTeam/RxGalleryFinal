package cn.finalteam.rxgalleryfinal.ui.widget;

import android.view.View;

/**
 * Desction:加载跟多UI展示
 * Author:pengjianbo
 * Date:16/5/14 下午5:55
 */
interface ILoadMoreView {
    /**
     * 显示普通布局
     */
    void showNormal();

    /**
     * 显示已经加载完成，没有更多数据的布局
     */
    void showNoMore();

    /**
     * 显示正在加载中的布局
     */
    void showLoading();

    /**
     * 获取footerview
     * @return
     */
    View getFooterView();
}
