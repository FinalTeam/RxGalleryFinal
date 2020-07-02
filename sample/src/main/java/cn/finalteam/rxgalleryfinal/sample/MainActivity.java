package cn.finalteam.rxgalleryfinal.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.sample.imageloader.ImageLoaderActivity;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.PermissionCheckUtils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton mRbRadioIMG, mRbMutiIMG, mRbOpenC, mRbRadioVD, mRbMutiVD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_image_loader).setOnClickListener(this);

        findViewById(R.id.btn_open_img).setOnClickListener(this);
        findViewById(R.id.btn_open_vd).setOnClickListener(this);

        mRbRadioIMG = (RadioButton) findViewById(R.id.rb_radio_img);
        mRbMutiIMG = (RadioButton) findViewById(R.id.rb_muti_img);
        mRbRadioVD = (RadioButton) findViewById(R.id.rb_radio_vd);
        mRbMutiVD = (RadioButton) findViewById(R.id.rb_muti_vd);
        mRbOpenC = (RadioButton) findViewById(R.id.rb_openC);
        //多选事件的回调
        RxGalleryListener
                .getInstance()
                .setMultiImageCheckedListener(
                        new IMultiImageCheckedListener() {
                            @Override
                            public void selectedImg(Object t, boolean isChecked) {
                                Toast.makeText(getBaseContext(), isChecked ? "选中" : "取消选中", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                                Toast.makeText(getBaseContext(), "你最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
                            }
                        });
        //裁剪图片的回调
        RxGalleryListener
                .getInstance()
                .setRadioImageCheckedListener(
                        new IRadioImageCheckedListener() {
                            @Override
                            public void cropAfter(Object t) {
                                Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public boolean isActivityFinish() {

                                return true;
                            }
                        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_image_loader:
                Intent intent = new Intent(v.getContext(), ImageLoaderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.btn_open_img:
                openImageSelect();
                break;
            case R.id.btn_open_vd:
                openVideoSelect();
                break;

        }

    }


    /**
     * 视频
     * 单选 多选
     */
    private void openVideoSelect() {
        if (mRbRadioVD.isChecked()) {
            openVideoSelectRadioMethod();
        } else if (mRbMutiVD.isChecked()) {
            openVideoSelectMultiMethod(0);
        }
    }

    /**
     * 图片
     * 单选，多选，  直接打开相机
     */
    private void openImageSelect() {

        if (mRbRadioIMG.isChecked()) {
            RxGalleryFinal
                    .with(MainActivity.this)
                    .image()
                    .radio()
                    .crop()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                        @Override
                        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                            Logger.i("只要选择图片就会触发");
                        }
                    })
                    .openGallery();
        } else if (mRbMutiIMG.isChecked()) {
            RxGalleryFinal
                    .with(MainActivity.this)
                    .image()
                    .multiple()
                    .maxSize(9)
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                        @Override
                        protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                            Logger.i("多选图片的回调");
                        }
                    })
                    .openGallery();
        } else {
            if (PermissionCheckUtils.checkCameraPermission(this, "", MediaActivity.REQUEST_CAMERA_ACCESS_PERMISSION)
                    && PermissionCheckUtils.checkWriteExternalPermission(this, "", MediaActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION)
                    && PermissionCheckUtils.checkReadExternalPermission(this, "", MediaActivity.REQUEST_STORAGE_READ_ACCESS_PERMISSION)) {
              //  RxGalleryFinalApi.openZKCamera(MainActivity.this);
            }
        }
    }

    private List<MediaBean> list = null;

    /**
     * 视频多选回调
     */
    private void openVideoSelectMultiMethod(int type) {
        RxGalleryFinal
                .with(MainActivity.this)
                .video()
                .radio()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Logger.i("只要选择图片就会触发");
                    }
                })
                .openGallery();
    }

    /**
     * 视频单选回调
     */
    private void openVideoSelectRadioMethod() {
        RxGalleryFinal
                .with(MainActivity.this)
                .video()
                .radio()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Logger.i("只要选择图片就会触发");
                    }
                })
                .openGallery();
    }

    /**
     * OPEN 图片多选实现方法
     * <p>
     * 默认使用 第三个 ，如果运行sample,可自行改变Type，运行Demo查看效果
     */
    private void openImageSelectMultiMethod(int type) {
        switch (type) {
            case 0:

                break;
            case 1:
                //使用自定义的参数

                break;
            case 2:

                break;
        }

    }

    /**
     * OPEN 图片单选实现方法
     * <p>
     * 默认使用 第三个 ，如果运行sample,可自行改变Type，运行Demo查看效果
     */
    private void openImageSelectRadioMethod(int type) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SimpleRxGalleryFinal.get().onActivityResult(requestCode, resultCode, data);
    }
}