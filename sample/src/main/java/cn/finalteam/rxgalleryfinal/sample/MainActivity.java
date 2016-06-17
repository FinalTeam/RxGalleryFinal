package cn.finalteam.rxgalleryfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;

public class MainActivity extends AppCompatActivity {

    Button mBtnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnOpen = (Button) findViewById(R.id.btn_open);

        mBtnOpen.setOnClickListener(v -> RxGalleryFinal
                .with(this)
                .image()
                .radio()
                .imageLoader(new PicassoImageLoader())
//                .pauseOnScrollListener()
                .openGallery());
    }
}
