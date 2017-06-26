package cn.finalteam.rxgalleryfinal.sample.imageloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Toast;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.sample.R;
import cn.finalteam.rxgalleryfinal.utils.Logger;

/**
 * by y on 2017/6/7.
 */

public class ImageLoaderActivity extends AppCompatActivity {

    private AppCompatCheckBox appCompatCheckBox;
    private RxGalleryFinal with;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);
        appCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_gif);
        findViewById(R.id.btn_glide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ImageLoaderType.GLIDE);
            }
        });
        findViewById(R.id.btn_picasso).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ImageLoaderType.PICASSO);
            }
        });
        findViewById(R.id.btn_fresco).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ImageLoaderType.FRESCO);
            }
        });
        findViewById(R.id.btn_universal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ImageLoaderType.UNIVERSAL);
            }
        });
    }

    private void start(ImageLoaderType imageLoaderType) {
        switch (imageLoaderType) {
            case PICASSO:
            case UNIVERSAL:
                Toast.makeText(getApplicationContext(), imageLoaderType + "不支持Gif", Toast.LENGTH_SHORT).show();
                break;
        }
        if (with == null)
            with = RxGalleryFinal.with(this);
        with.image()
                .radio()
                .gif(appCompatCheckBox.isChecked())
                .imageLoader(imageLoaderType)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Toast.makeText(getBaseContext(), "选中了图片路径：" + imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                    }
                }).openGallery();
    }

    @Override
    protected void onDestroy() {
        if (with != null) {
            Logger.i("RxGalleryFinal == null");
            with = null;
        }
        super.onDestroy();
    }
}
