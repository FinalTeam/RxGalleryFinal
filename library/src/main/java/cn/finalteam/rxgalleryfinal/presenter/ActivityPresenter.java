package cn.finalteam.rxgalleryfinal.presenter;

import android.app.Activity;

import javax.inject.Inject;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 下午10:58
 */
public class ActivityPresenter {

    private final Activity activity;

    /**
     * 需要注入
     * @param activity
     */
    @Inject
    public ActivityPresenter(Activity activity) {
        this.activity = activity;
    }

}
