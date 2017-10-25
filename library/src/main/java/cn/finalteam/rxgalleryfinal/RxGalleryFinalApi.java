package cn.finalteam.rxgalleryfinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaGridFragment;
import cn.finalteam.rxgalleryfinal.utils.Logger;
import cn.finalteam.rxgalleryfinal.utils.MediaScanner;
import cn.finalteam.rxgalleryfinal.utils.SimpleDateUtils;

/**
 * 设置回调
 * Created by KARL-dujinyang on 2017-03-23 03-03-00.
 */
public class RxGalleryFinalApi {
    public static final int TAKE_IMAGE_REQUEST_CODE = 19001;
    private static String IMG_TYPE = "image/jpeg";
    public static File fileImagePath;//拍照图片
    public static File cropImagePath;//裁剪图片
    private static RxGalleryFinalApi mRxApi = new RxGalleryFinalApi();
    private static RxGalleryFinal rxGalleryFinal;


    /**
     * 默认使用 ImageLoaderType.GLIDE
     */
    public static RxGalleryFinalApi getInstance(Activity context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        rxGalleryFinal = RxGalleryFinal.with(context)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(null);
        Logger.i("==========" + mRxApi + "====" + rxGalleryFinal);
        return mRxApi;
    }

    /**
     * 单选图片
     *
     * @param flag 标识是否开启裁剪
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static RxGalleryFinalApi openRadioSelectImage(Activity context, RxBusResultDisposable<ImageRadioResultEvent> rxBusResultDisposable, boolean flag) {
        getInstance(context);
        if (flag) {
            rxGalleryFinal
                    .image()
                    .radio()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(rxBusResultDisposable)
                    .openGallery();
        } else {
            rxGalleryFinal
                    .image()
                    .radio()
                    .crop()
                    .imageLoader(ImageLoaderType.GLIDE)
                    .subscribe(rxBusResultDisposable)
                    .openGallery();
        }
        return mRxApi;
    }

    /**
     * 得到裁剪之后的事件
     *
     * @return RxGalleryFinalApi
     */
    public RxGalleryFinalApi onCropImageResult(IRadioImageCheckedListener listener) {
        RxGalleryListener.getInstance().setRadioImageCheckedListener(listener);
        return mRxApi;
    }

    /**
     * 得到裁剪之后的事件
     *
     * @return RxGalleryFinalApi
     */
    public static RxGalleryFinalApi onMultiImageResult(IMultiImageCheckedListener listener) {
        RxGalleryListener.getInstance().setMultiImageCheckedListener(listener);
        return mRxApi;
    }

    /**
     * 得到多选限制事件
     */
    public static RxGalleryFinalApi onCrop(boolean flag) {
        if (rxGalleryFinal == null)
            return null;
        rxGalleryFinal.crop(flag);
        return mRxApi;
    }

    /**
     * 单选默认设置
     */
    public RxGalleryFinalApi openGalleryRadioImgDefault(RxBusResultDisposable<ImageRadioResultEvent> rxBusResultDisposable) {
        Logger.i("----rxGalleryFinal---" + rxGalleryFinal);
        if (rxGalleryFinal == null)
            return null;
        rxGalleryFinal
                .image()
                .radio()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultDisposable)
                .openGallery();
        return mRxApi;
    }

    /**
     * 得到多选限制事件
     */
    public static RxGalleryFinalApi openGallery() {
        if (rxGalleryFinal == null)
            return null;
        rxGalleryFinal.openGallery();
        return mRxApi;
    }

    /**
     * 单选图片:默认开启全部
     *
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static void openRadioSelectImage(Activity context, RxBusResultDisposable rxBusResultDisposable) {
        RxGalleryFinal
                .with(context)
                .image()
                .radio()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultDisposable)
                .openGallery();
    }

    /**
     * 多选图片：默认开启全部
     *
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static void openMultiSelectImage(Activity context, RxBusResultDisposable<ImageMultipleResultEvent> rxBusResultDisposable) {
        RxGalleryFinal
                .with(context)
                .image()
                .multiple()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultDisposable)
                .openGallery();
    }

    /**
     * 多选图片：
     *
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static void openMultiSelectImage(Activity context, int maxSize, RxBusResultDisposable<ImageMultipleResultEvent> rxBusResultDisposable) {
        RxGalleryFinal
                .with(context)
                .image()
                .maxSize(maxSize)
                .multiple()
                .crop()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultDisposable)
                .openGallery();
    }

    /**
     * 单选视频:默认开启全部
     *
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static void openRadioSelectVD(Activity context, RxBusResultDisposable<ImageRadioResultEvent> rxBusResultDisposable) {
        RxGalleryFinal
                .with(context)
                .multiple()
                .video()
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(rxBusResultDisposable)
                .openGallery();
    }

    /**
     * 多选视频 ：默认开启全部
     * 默认选9个视频
     *
     * @Override protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
     * <p>
     * }
     * }
     * @see new RxBusResultSubscriber<ImageRadioResultEvent>() {
     */
    public static void openMultiSelectVD(Activity context, RxBusResultDisposable<ImageMultipleResultEvent> rxBusResultDisposable) {
        RxGalleryFinal
                .with(context)
                .video()
                .multiple()
                .maxSize(9)
                .imageLoader(ImageLoaderType.UNIVERSAL)
                .subscribe(rxBusResultDisposable)
                .openGallery();
    }

    /**
     * 打开相机
     * <p>
     * fragment 或者 activity 直接传入  this，  内部处理
     * <p>
     * 这里的 Fragment 指的是 v4包下的Fragment
     *
     * @see Fragment
     * <p>
     * M 以上的权限处理，拍照以及读取相册需要自行申请，这里不做处理
     * <p>
     * 返回值： -1 ： 设备没有相机
     */
    public static int openZKCamera(Object activity) {

        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        Activity cameraActivity = null;
        if (activity instanceof Activity) {
            cameraActivity = (Activity) activity;
        }
        if (activity instanceof Fragment) {
            Fragment fragment = (Fragment) activity;
            cameraActivity = fragment.getActivity();
        }
        assert cameraActivity != null;
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(cameraActivity.getPackageManager()) != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String imageName = "immqy_%s.jpg";
            String filename = String.format(imageName, dateFormat.format(new Date()));
            File mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/IMMQY/");
            if (!mImageStoreDir.exists()) {
                mImageStoreDir.mkdirs();
            }
            fileImagePath = new File(mImageStoreDir, filename);
            String mImagePath = fileImagePath.getAbsolutePath();
            Logger.i("->mImagePath:" + mImagePath);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagePath));
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mImagePath);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, IMG_TYPE);
                Uri uri = cameraActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            if (activity instanceof Activity) {
                cameraActivity.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
            }
            if (activity instanceof Fragment) {
                Fragment fragment = (Fragment) activity;
                fragment.startActivityForResult(captureIntent, TAKE_IMAGE_REQUEST_CODE);
            }
            return 0;
        } else {
            Toast.makeText(cameraActivity, R.string.gallery_device_camera_unable, Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    /**
     * 处理拍照返回
     */
    public static void openZKCameraForResult(Activity context, MediaScanner.ScanCallback mediaScanner) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            MediaScanner scanner = new MediaScanner(context);
            scanner.scanFile(RxGalleryFinalApi.fileImagePath.getPath(), IMG_TYPE, mediaScanner);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.ImageColumns.TITLE, "title");
            values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "filename.jpg");
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.ImageColumns.MIME_TYPE, IMG_TYPE);
            values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
            values.put(MediaStore.Images.ImageColumns.DATA, RxGalleryFinalApi.fileImagePath.getPath());
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
                Logger.e("Failed to write MediaStore" + e);
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + RxGalleryFinalApi.fileImagePath.getPath())));
        }
    }

    /**
     * 快速生成图片的路径
     *
     * @return 虚拟路径
     */
    public static String getModelPath() {
        File fileImagePath = null;
        try {
            String imageName = "immqy_%s_%s.jpg";
            Random random = new Random();
            String filename = String.format(imageName, SimpleDateUtils.getNowTime(), "" + random.nextInt(1024));
            File mImageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/IMMQY/");
          /*  if(!mImageStoreDir.exists()){
                mImageStoreDir.mkdirs();
            }*/
            fileImagePath = new File(mImageStoreDir, filename);
            //mImagePath = fileImagePath.getPath();
            Logger.i("Test Path:" + fileImagePath.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("e=>" + e.getMessage());
        }
        return fileImagePath.getPath();
    }

    /**
     * 裁剪指定的路径-
     * onActivityResult或者其它地方调用RxGalleryFinalApi.cropActivityForResult方法去刷新图库
     * RxGalleryFinalApi.cropActivityForResult()
     */
    public static void cropScannerForResult(Activity context, String outPPath, String inputPath) {
        if (TextUtils.isEmpty(inputPath)) {
            Logger.e("-裁剪没有图片-");
            return;
        }
        Uri outUri = Uri.fromFile(new File(outPPath));
        Uri inputUri = Uri.fromFile(new File(inputPath));
        Intent intent = new Intent(context, UCropActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, outUri);
        bundle.putParcelable(UCrop.EXTRA_INPUT_URI, inputUri);
        cropImagePath = new File(outUri.getPath());
        Logger.i("输出：" + outUri.getPath());
        Logger.i("原图：" + inputUri.getPath());
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivityForResult(intent, -1);//无效
    }

    /**
     * 扫描指定的图片路径--刷新图库
     */
    public static void cropActivityForResult(Activity context, MediaScanner.ScanCallback imgScanner) {
        if (cropImagePath != null) {
            MediaScanner scanner = new MediaScanner(context);
            scanner.scanFile(RxGalleryFinalApi.cropImagePath.getPath(), IMG_TYPE, imgScanner);
        }
    }

    /**
     * 扫描指定的图片路径--刷新图库
     *
     * @param path 路径
     */
    public static void cropActivityForResult(Activity context, String path, MediaScanner.ScanCallback imgScanner) {
        if (cropImagePath != null) {
            MediaScanner scanner = new MediaScanner(context);
            scanner.scanFile(path.trim(), IMG_TYPE, imgScanner);
        }
    }

    /**
     * 设置图片存储的路径
     *
     * @param file 返回原来的File
     */
    public static File setImgSaveRxDir(File file) {
        MediaGridFragment.setImageStoreDir(file);
        return file;
    }
    //***********************************************************************//

    /**
     * 设置图片存储到sd卡 -- 取文件夹名称
     */
    public static void setImgSaveRxSDCard(String name) {
        MediaGridFragment.setImageStoreDir(name);
    }

    /**
     * 设置裁剪存储的路径
     *
     * @param file 返回原来的File
     */
    public static File setImgSaveRxCropDir(File file) {
        MediaGridFragment.setImageStoreCropDir(file);
        return file;
    }

    /**
     * 设置裁剪存储到sd卡 -- 取文件夹名称
     */
    public static void setImgSaveRxCropSDCard(String name) {
        MediaGridFragment.setImageStoreCropDir(name);
    }

    /**
     * 获取裁剪存储的路径
     */
    public static String getImgSaveRxCropDirByStr() {
        return MediaGridFragment.getImageStoreCropDirByStr();
    }

    /**
     * 获取裁剪存储的路径
     *
     * @return String
     */
    public static File getImgSaveRxCropDirByFile() {
        return MediaGridFragment.getImageStoreCropDirByFile();
    }

    /**
     * 获取图片存储的路径
     *
     * @return String 路径
     */
    public static String getImgSaveRxDirByStr() {
        return MediaGridFragment.getImageStoreDirByStr();
    }

    /**
     * 获取图片存储的路径
     *
     * @return File 路径
     */
    public static File getImgSaveRxDirByFile() {
        return MediaGridFragment.getImageStoreDirByFile();
    }

    /**
     * 设置打开的类型和方式--多选默认9张图
     * setType(SelectRXType.TYPE_IMAGE,SelectRXType.TYPE_SELECT_RADIO);
     * setType(SelectRXType.TYPE_VIDEO,SelectRXType.TYPE_SELECT_RADIO);
     *
     * @param type 图片或者视频
     * @param mt   单选或者多选 .多选默认9张图
     */
    public RxGalleryFinalApi setType(int type, int mt) {
        switch (type) {
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
        switch (mt) {
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
     */
    public RxGalleryFinalApi setImageRadioResultEvent(RxBusResultDisposable<ImageRadioResultEvent> t) {
        rxGalleryFinal.image();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    /**
     * 设置多选的按钮事件
     */
    public RxGalleryFinalApi setImageMultipleResultEvent(RxBusResultDisposable<ImageMultipleResultEvent> t) {
        rxGalleryFinal.image();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    /**
     * 设置视频单选的按钮事件
     */
    public RxGalleryFinalApi setVDRadioResultEvent(RxBusResultDisposable<ImageRadioResultEvent> t) {
        rxGalleryFinal.video();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    /**
     * 设置视频多选的按钮事件
     */
    public RxGalleryFinalApi setVDMultipleResultEvent(RxBusResultDisposable<ImageMultipleResultEvent> t) {
        rxGalleryFinal.video();
        rxGalleryFinal.subscribe(t);
        return mRxApi;
    }

    public RxGalleryFinalApi setCrop() {
        rxGalleryFinal.crop();
        return mRxApi;
    }

    /**
     * 直接打开默认设置好的参数
     */
    public RxGalleryFinalApi open() {
        rxGalleryFinal.openGallery();
        return mRxApi;
    }

    /**
     * 选择类型
     */
    public static class SelectRXType {
        public static final int TYPE_IMAGE = 801;
        public static final int TYPE_VIDEO = 702;
        public static final int TYPE_SELECT_RADIO = 1;
        public static final int TYPE_SELECT_MULTI = 2;
    }

}