package cn.finalteam.rxgalleryfinal.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/16 下午7:36
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxGalleryFinalComponent rxGalleryFinalComponent = RxGalleryFinal.getRxGalleryFinalComponent();
        if(rxGalleryFinalComponent == null) {
            mFinishHanlder.sendEmptyMessageDelayed(0, 500);
            return;
        }
        setupComponent(rxGalleryFinalComponent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme(getTheme());
    }

    protected Handler mFinishHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
        }
    };

    protected abstract void setTheme(Resources.Theme theme);

    protected abstract void setupComponent(RxGalleryFinalComponent rxGalleryFinalComponent);
}
