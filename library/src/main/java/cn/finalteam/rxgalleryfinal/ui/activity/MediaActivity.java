package cn.finalteam.rxgalleryfinal.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import cn.finalteam.rxgalleryfinal.di.module.Configuration;
import cn.finalteam.rxgalleryfinal.R;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 上午10:01
 */
public class MediaActivity extends AppCompatActivity {

    @Inject
    Configuration mConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryfinal_activity_media);
    }
}
