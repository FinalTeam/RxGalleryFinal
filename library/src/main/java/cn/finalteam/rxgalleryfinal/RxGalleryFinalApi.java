package cn.finalteam.rxgalleryfinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;

/**
 * 设置回调
 * Created by KARL-dujinyang on 2017-03-23 03-03-00.
 */
public class RxGalleryFinalApi {
    static RxGalleryFinalApi mRxApi;
    private static RxGalleryFinal rxGalleryFinal;
    private static Activity mActivity;


    /**
     * 默认使用 ImageLoaderType.GLIDE
     * @param context
     * @return  RxGalleryFinalApi
     */
    public static RxGalleryFinalApi getInstance(Activity context){
        if(context==null) {
           return null;
        }
        if(mRxApi == null) {
            mRxApi = new RxGalleryFinalApi();
        }
            mActivity = context;
            rxGalleryFinal = rxGalleryFinal.with(context)
                  //  .image()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(null) ;

        Logger.i("=========="+mRxApi+"===="+rxGalleryFinal);
        return mRxApi;
    }



    /**
     * 选择类型
     */
    public static class SelectRXType {
        public static final int TYPE_IMAGE=801;
        public static final int TYPE_VIDEO=702;
        public static final int TYPE_SELECT_RADIO = 1;
        public static final int TYPE_SELECT_MULTI = 2;
    }

    /**
     *设置打开的类型和方式--多选默认9张图
     * setType(SelectRXType.TYPE_IMAGE,SelectRXType.TYPE_SELECT_RADIO);
     * setType(SelectRXType.TYPE_VIDEO,SelectRXType.TYPE_SELECT_RADIO);
     * @param type 图片或者视频
     * @param mt 单选或者多选 .多选默认9张图
     */
    public RxGalleryFinalApi setType(int type,int mt){
        switch (type){
            case SelectRXType.TYPE_IMAGE:
                rxGalleryFinal.image();
                break;
            case SelectRXType.TYPE_VIDEO:
                rxGalleryFinal.video();
                break;
            default:
                Logger.e("open type is error!!!");
                break;
        }
        switch (mt){
            case SelectRXType.TYPE_SELECT_RADIO:
                rxGalleryFinal.radio();
                break;
            case SelectRXType.TYPE_SELECT_MULTI:
                rxGalleryFinal.multiple();
                rxGalleryFinal.maxSize(9);
                break;
            default:
                Logger.e("open mt is error!!!");
                break;
        }
        return mRxApi;
    }


    /**
     * 设置单选的按钮事件
     * @param t
     */
    public RxGalleryFinalApi setImageRadioResultEvent(RxBusResultSubscriber<ImageRadioResultEvent> t){
        rxGalleryFinal.image();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    /**
     * 设置多选的按钮事件
     * @param t
     */
    public RxGalleryFinalApi setImageMultipleResultEvent(RxBusResultSubscriber<ImageMultipleResultEvent> t){
        rxGalleryFinal.image();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }


    /**
     * 设置视频单选的按钮事件
     * @param t
     */
    public RxGalleryFinalApi setVDRadioResultEvent(RxBusResultSubscriber<ImageRadioResultEvent> t){
        rxGalleryFinal.video();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    /**
     * 设置视频多选的按钮事件
     * @param t
     */
    public RxGalleryFinalApi setVDMultipleResultEvent(RxBusResultSubscriber<ImageMultipleResultEvent> t){
        rxGalleryFinal.video();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    public RxGalleryFinalApi setCrop(){
        rxGalleryFinal.crop();
        return mRxApi;
    }

    /**
     * 直接打开默认设置好的参数
     */
    public RxGalleryFinalApi open(){
        rxGalleryFinal.openGallery();
        return mRxApi;
    }




    /**
     * 单选图片
     *@see    new RxBusResultSubscriber<ImageRadioResultEvent>() {
     *         @Override
     *        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     *
     *       }
     *    }
     * @param context
     * @param rxBusResultSubscriber
     * @param flag 标识是否开启裁剪
     */
    public static RxGalleryFinal openRadioSelectImage(Activity context,RxBusResultSubscriber rxBusResultSubscriber,boolean flag){

        if(flag) {
            rxGalleryFinal
                    .image()
                    .radio()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(rxBusResultSubscriber)
                    .openGallery();
        }else{
            rxGalleryFinal
                    .image()
                    .radio()
                    .crop()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(rxBusResultSubscriber)
                    .openGallery();
        }
        return rxGalleryFinal;
    }

    /**
     * 单选图片:默认开启全部
     *@see    new RxBusResultSubscriber<ImageRadioResultEvent>() {
     *         @Override
     *        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     *
     *       }
     *    }
     * @param context
     * @param rxBusResultSubscriber
     */
    public static void openRadioSelectImage(Activity context,RxBusResultSubscriber rxBusResultSubscriber){
            RxGalleryFinal
                    .with(context)
                    .image()
                    .radio()
                    .crop()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(rxBusResultSubscriber)
                    .openGallery();
    }


    /**
     * 多选图片：默认开启全部
     *  @see    new RxBusResultSubscriber<ImageRadioResultEvent>() {
     *         @Override
     *        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     *
     *       }
     *    }
     * @param context
     * @param rxBusResultSubscriber
     */
    public static void openMultiSelectImage(Activity context,RxBusResultSubscriber rxBusResultSubscriber){
        RxGalleryFinal
                .with(context)
                .image()
                .multiple()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultSubscriber)
                .openGallery();
    }

    /**
     * 单选视频:默认开启全部
     *@see    new RxBusResultSubscriber<ImageRadioResultEvent>() {
     *         @Override
     *        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     *
     *       }
     *    }
     * @param context
     * @param rxBusResultSubscriber
     */
    public static void openRadioSelectVD(Activity context,RxBusResultSubscriber rxBusResultSubscriber){
        RxGalleryFinal
                .with(context)
                .multiple()
                .video()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultSubscriber)
                .openGallery();
    }

    /**
     * 多选视频 ：默认开启全部
     * 默认选9个视频
     *  @see    new RxBusResultSubscriber<ImageRadioResultEvent>() {
     *         @Override
     *        protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     *
     *       }
     *    }
     * @param context
     * @param rxBusResultSubscriber
     */
    public static void openMultiSelectVD(Activity context,RxBusResultSubscriber rxBusResultSubscriber){
        RxGalleryFinal
                .with(context)
                .video()
                .multiple()
                .maxSize(9)
                .imageLoader(ImageLoaderType.UNIVERSAL)
                .subscribe(rxBusResultSubscriber)
                .openGallery();
    }



    //***********************************************************************//
    public static final int TAKE_IMAGE_REQUEST_CODE = 19001;
    public static File fileImagePath;
    private static int currentapiVersion = 0;
    //***********************************************************************//
    /**
     * @Author:Karl-dujinyang
     * 兼容7.0打开路径问题
     */
    public static void openZKCamera(Activity context) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String imageName = "immqy_%s.jpg";
            String filename = String.format(imageName, dateFormat.format(new Date()));
            File mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/IMMQY/");;
            if(!mImageStoreDir.exists()){
                mImageStoreDir.mkdirs();
            }
            fileImagePath = new File(mImageStoreDir, filename);
            String mImagePath = fileImagePath.getAbsolutePath();
            Logger.i("->test:"+mImagePath);
            if(mImagePath!=null){
                    /*获取当前系统的android版本号*/
                currentapiVersion = android.os.Build.VERSION.SDK_INT;
                Logger.i("->test:"+currentapiVersion);
                if (currentapiVersion<24){
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
                    /*captureIntent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");*/
                    Logger.i("->test:" + Uri.fromFile(fileImagePath));
                    context.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
                }else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mImagePath );
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    context.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
                }
            }
        } else {
            Toast.makeText(context, R.string.gallery_device_camera_unable, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理拍照返回
     * @param context
     * @param mediaScanner
     */
    public static void openZKCameraForResult(Activity context,MediaScanner.ScanCallback mediaScanner){
        if(currentapiVersion<24){
            MediaScanner scanner = new MediaScanner(context);
            scanner.scanFile(RxGalleryFinalApi.fileImagePath.getPath().toString(), "image/jpeg", mediaScanner);
        }else{
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.ImageColumns.TITLE, "title");
            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "filename.jpg");
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
            values.put(MediaStore.Images.ImageColumns.DATA, RxGalleryFinalApi.fileImagePath.getPath().toString());
           /* values.put(MediaStore.Images.ImageColumns.WIDTH, bmp.getWidth());
            values.put(MediaStore.Images.ImageColumns.HEIGHT, bmp.getHeight());*/
            try {
                Uri uri = context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri == null) {
                    Logger.e("Failed to insert MediaStore");
                } else {
                    context.sendBroadcast(new Intent(
                            "com.android.camera.NEW_PICTURE", uri));
                }
            } catch (Exception e) {
                Logger.e("Failed to write MediaStore"+ e);
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + RxGalleryFinalApi.fileImagePath.getPath().toString())));
        }
    }


}
