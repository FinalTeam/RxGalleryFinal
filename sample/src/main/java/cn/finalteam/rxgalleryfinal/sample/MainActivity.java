package cn.finalteam.rxgalleryfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

public class MainActivity extends AppCompatActivity {

    RadioButton mRbRadio,mRbMuti;
    Button mBtnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        .imageLoader(ImageLoaderType.PICASSO)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Toast.makeText(getBaseContext(), imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .openGallery();
            } else {
                RxGalleryFinal
                        .with(MainActivity.this)
                        .image()
                        .multiple()
                        .maxSize(8)
                        .imageLoader(ImageLoaderType.PICASSO)
                        .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                            @Override
                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                Toast.makeText(getBaseContext(), "已选择" + imageMultipleResultEvent.getResult().size() +"张图片", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .openGallery();
            }
        });

    }


}
