package cn.finalteam.rxgalleryfinal.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.OpenMediaPreviewFragmentEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;
import cn.finalteam.rxgalleryfinal.utils.ModelUtils;

/**
 * 示例
 * @author KARL-dujinyang
 */
public class MainActivity extends AppCompatActivity {

    RadioButton mRbRadioIMG, mRbMutiIMG, mRbOpenC,mRbRadioVD,mRbMutiVD;
    Button mBtnOpenDefRadio,mBtnOpenDefMulti,mBtnOpenIMG,mBtnOpenVD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //手动打开日志。
        ModelUtils.setDebugModel(true);
        initView();
        initImageLoader();
        initFresco();
        //自定义使用
        initClickZDListener();
        //调用图片选择器Api
        initClickSelImgListener();
        //调用视频选择器Api
        initClickSelVDListener();
        //多选事件的回调
        getMultiListener();
    }

    /**
     *  调用视频选择器Api
     */
    private void initClickSelVDListener() {
        mBtnOpenVD.setOnClickListener(view -> {
            if (mRbRadioVD.isChecked()) {
                RxGalleryFinalApi.getInstance(this)
                        .setType(RxGalleryFinalApi.SelectRXType.TYPE_VIDEO, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO)
                        .setVDRadioResultEvent(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                 //回调还没

                            }
                        })
                        .open();



            } else if (mRbMutiVD.isChecked()) {
                //多选图片的方式
                //1.使用默认的参数
              /*  RxGalleryFinalApi.getInstance(this).setVDMultipleResultEvent(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        Logger.i("多选视频的回调");
                    }
                }).open();
*/
                //2.使用自定义的参数
                RxGalleryFinalApi.getInstance(this)
                        .setType(RxGalleryFinalApi.SelectRXType.TYPE_VIDEO, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_MULTI)
                        .setVDMultipleResultEvent(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                            @Override
                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                Logger.i("多选视频的回调");
                            }
                        }).open();;

                //3.直接打开
            /*    RxGalleryFinalApi.openMultiSelectVD(this, new RxBusResultSubscriber() {
                    @Override
                    protected void onEvent(Object o) throws Exception {
                        Logger.i("多选视频的回调");
                    }
                });*/
            }
        });
    }

    /**
     *  调用图片选择器Api
     */
    private void initClickSelImgListener() {
        mBtnOpenIMG.setOnClickListener(view -> {
            if (mRbRadioIMG.isChecked()) {
                //以下方式 -- 可选：
                //1.打开单选图片，默认参数
                RxGalleryFinalApi.getInstance(this).setImageRadioResultEvent(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Logger.i("单选图片的回调");
                    }
                }).open();

                //2.设置自定义的参数
                RxGalleryFinalApi.getInstance(this).setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_RADIO)
                .setImageRadioResultEvent(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                        Logger.i("单选图片的回调");
                    }
                }).open();

                //3.打开单选图片
                RxGalleryFinalApi.openRadioSelectImage(this, new RxBusResultSubscriber() {
                    @Override
                    protected void onEvent(Object o) throws Exception {

                    }
                });

            } else if (mRbMutiIMG.isChecked()) {
                //多选图片的方式
                //1.使用默认的参数
                RxGalleryFinalApi.getInstance(this).setImageMultipleResultEvent(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        Logger.i("多选图片的回调");
                    }
                }).open();

                //2.使用自定义的参数
                RxGalleryFinalApi.getInstance(this)
                        .setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_MULTI)
                        .setImageMultipleResultEvent(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                            @Override
                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                Logger.i("多选图片的回调");
                            }
                        }).open();;

                //3.直接打开
                RxGalleryFinalApi.openMultiSelectImage(this, new RxBusResultSubscriber() {
                    @Override
                    protected void onEvent(Object o) throws Exception {
                        Logger.i("多选图片的回调");
                    }
                });
            } else {
                //直接打开相机
                RxGalleryFinalApi.openZKCamera(MainActivity.this);
            }
        });
    }


    /**
     * 如果使用api定义好的，则自己定义使用
     * ImageLoaderType :自己选择使用
     */
    private void initClickZDListener() {
        mBtnOpenDefRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单选图片
                RxGalleryFinal
                        .with(MainActivity.this)
                        .image()
                        .radio()
                        .crop()
                        .imageLoader(ImageLoaderType.FRESCO)
                        .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                            @Override
                            protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
                                Toast.makeText(getBaseContext(), imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .openGallery();
            }
        });

        mBtnOpenDefMulti.setOnClickListener(view-> {
            //多选图片
            RxGalleryFinal
                    .with(MainActivity.this)
                    .image()
                    .multiple()
                    .maxSize(8)
                    .imageLoader(ImageLoaderType.UNIVERSAL)
                    .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                        @Override
                        protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                            Toast.makeText(getBaseContext(), "已选择" + imageMultipleResultEvent.getResult().size() + "张图片", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                            Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .openGallery();

        });
    }


    /**
     * 多选事件都会在这里执行
     */
    public void getMultiListener(){
        //得到多选的事件
        RxGalleryListener.getInstance().setMultiImageCheckedListener(new IMultiImageCheckedListener() {
            @Override
            public void selectedImg(Object t, boolean isChecked) {
                //这个主要点击或者按到就会触发，所以不建议在这里进行Toast
            }

            @Override
            public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                Toast.makeText(getBaseContext(), "你最多只能选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ID
    private void initView() {
        mBtnOpenIMG = (Button) findViewById(R.id.btn_open_img);
        mBtnOpenVD = (Button) findViewById(R.id.btn_open_vd);
        mBtnOpenDefRadio = (Button) findViewById(R.id.btn_open_def_radio);
        mBtnOpenDefMulti = (Button) findViewById(R.id.btn_open_def_multi);
        mRbRadioIMG = (RadioButton) findViewById(R.id.rb_radio_img);
        mRbMutiIMG = (RadioButton) findViewById(R.id.rb_muti_img);
        mRbRadioVD = (RadioButton) findViewById(R.id.rb_radio_vd);
        mRbMutiVD = (RadioButton) findViewById(R.id.rb_muti_vd);
        mRbOpenC = (RadioButton) findViewById(R.id.rb_openC);
    }

    //ImageLoaderConfiguration
    private void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    //Fresco
    private void initFresco() {
        Fresco.initialize(this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + " data:" + data);
            if (requestCode == RxGalleryFinalApi.TAKE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Logger.i("拍照OK，图片路径:"+RxGalleryFinalApi.fileImagePath.getPath().toString());
                //刷新相册数据库
                RxGalleryFinalApi.openZKCameraForResult(MainActivity.this, new MediaScanner.ScanCallback() {
                    @Override
                    public void onScanCompleted(String[] strings) {
                        Logger.i(String.format("拍照成功,图片存储路径:%s", strings[0]));
                    }
                });
            } else {
                Logger.i("失敗");
            }
    }


}