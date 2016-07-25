package cn.finalteam.rxgalleryfinal.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;

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
                .multiple()
                .maxSize(8)
                .imageLoader(ImageLoaderType.PICASSO)
                .openGallery());
//
//        mBtnOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String path = "/storage/emulated/0/com.ct.client/crop/1468247716552.jpg";
//
//                File file = new File(path);
//                File saveFile = new File(getCacheDir(), file.getName());
//                System.out.println("====" + saveFile.getAbsolutePath());
//                Uri uri = Uri.fromFile(file);
//                UCrop uCrop = UCrop.of(uri, Uri.fromFile(saveFile));
//                uCrop = uCrop.useSourceImageAspectRatio();
//                UCrop.Options options = new UCrop.Options();
//                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//                options.setCompressionQuality(50);
//                options.setFreeStyleCropEnabled(true);
//                uCrop = uCrop.withOptions(options);
//                uCrop.start(MainActivity.this);
//
//                UCrop.of(uri, Uri.fromFile(saveFile))
//                        .withAspectRatio(16, 9)
//                        .withMaxResultSize(500, 500)
//                        .start(MainActivity.this);
//            }
//        });

    }


}
