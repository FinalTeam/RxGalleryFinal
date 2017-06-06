package cn.finalteam.rxgalleryfinal.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.finalteam.rxgalleryfinal.BuildConfig;
import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.utils.Logger;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/16 下午7:36
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;
    public static final String EXTRA_CONFIGURATION = EXTRA_PREFIX + ".Configuration";

    private final String CLASS_NAME = getClass().getSimpleName();

    public Configuration mConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printActivityLife("onCreate");
        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }


        if (savedInstanceState != null) {
            mConfiguration = savedInstanceState.getParcelable(EXTRA_CONFIGURATION);
        }
        if (mConfiguration == null && bundle != null) {
            mConfiguration = bundle.getParcelable(EXTRA_CONFIGURATION);
        }

        if (mConfiguration == null) {
            finish();
        } else {
            if (bundle == null) {
                bundle = savedInstanceState;
            }
            setContentView(getContentView());
            findViews();
            setTheme();
            onCreateOk(bundle);
        }
    }

    @LayoutRes
    public abstract int getContentView();

    protected abstract void onCreateOk(@Nullable Bundle savedInstanceState);

    @Override
    protected void onStart() {
        super.onStart();
        printActivityLife("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        printActivityLife("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        printActivityLife("onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        printActivityLife("onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printActivityLife("onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        printActivityLife("onSaveInstanceState");
        outState.putParcelable(EXTRA_CONFIGURATION, mConfiguration);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        printActivityLife("onRestoreInstanceState");
        mConfiguration = savedInstanceState.getParcelable(EXTRA_CONFIGURATION);
    }

    public abstract void findViews();

    protected abstract void setTheme();

    private void printActivityLife(String method) {
        Logger.i(String.format("Activity:%s Method:%s", CLASS_NAME, method));
    }
}
