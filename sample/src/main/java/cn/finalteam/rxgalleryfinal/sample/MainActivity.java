package cn.finalteam.rxgalleryfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    RadioButton mRbRadio,mRbMuti;
    Button mBtnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader();
        initFresco();

        mBtnOpen = (Button) findViewById(R.id.btn_open);
        mRbRadio = (RadioButton) findViewById(R.id.rb_radio);
        mRbMuti = (RadioButton) findViewById(R.id.rb_muti);

        mBtnOpen.setOnClickListener(view -> {
            if(mRbRadio.isChecked()) {
                RxGalleryFinal
                        .with(MainActivity.this)
                        .image()
                        .radio()
                        .crop()
                        .imageLoader(ImageLoaderType.FRESCO)
                        .asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            ImageRadioResultEvent imageRadioResultEvent = (ImageRadioResultEvent) data;
                            Toast.makeText(getBaseContext(), imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                            Logger.d("原始："+imageRadioResultEvent.getResult().getOriginalPath());
                            Logger.d("裁剪："+imageRadioResultEvent.getResult().getCropPath());
                        });
            } else {
                RxGalleryFinal
                        .with(MainActivity.this)
                        .image()
                        .multiple()
                        .maxSize(8)
                        .imageLoader(ImageLoaderType.FRESCO)
                        .asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            ImageMultipleResultEvent imageMultipleResultEvent = (ImageMultipleResultEvent) data;
                            Toast.makeText(getBaseContext(), "已选择" + imageMultipleResultEvent.getResult().size() +"张图片", Toast.LENGTH_SHORT).show();
                            Logger.d("已选择" + imageMultipleResultEvent.getResult().size() +"张图片");
                        });
            }
        });

    }

    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    private void initFresco() {
        Fresco.initialize(this);
    }


}
