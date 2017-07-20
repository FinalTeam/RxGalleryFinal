package cn.finalteam.rxgalleryfinal.sample;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * by y on 17/07/2017.
 * <p>
 * 使用相机拍照并裁剪
 */

public class SimpleRxGalleryFinal {

    private static final String IMAGE_TYPE = "image/jpeg";
    private static final int TYPE_CAMERA = 1111;

    private RxGalleryFinalCropListener listener = null;

    private Uri imagePath;


    private static final class SimpleRxGalleryFinalHolder {
        private static final SimpleRxGalleryFinal SIMPLE_RX_GALLERY_FINAL = new SimpleRxGalleryFinal();
    }

    public static SimpleRxGalleryFinal get() {
        return SimpleRxGalleryFinalHolder.SIMPLE_RX_GALLERY_FINAL;
    }


    public SimpleRxGalleryFinal init(RxGalleryFinalCropListener listener) {
        this.listener = listener;
        return this;
    }


    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = Uri.fromFile(getDiskCacheDir());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, imagePath.getPath());
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, IMAGE_TYPE);
            Uri uri = listener.getSimpleActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        listener
                .getSimpleActivity()
                .startActivityForResult(intent, TYPE_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_CANCELED:
                listener.onCropCancel();
                break;
            case UCrop.RESULT_ERROR:
                if (data != null) {
                    Throwable cropError = UCrop.getError(data);
                    if (cropError != null) {
                        listener.onCropError(cropError.getMessage());
                    } else {
                        listener.onCropError("裁剪出现未知错误");
                    }
                } else {
                    listener.onCropError("获取相册图片出现错误");
                }
                break;

            case Activity.RESULT_OK:
                switch (requestCode) {
                    case TYPE_CAMERA:
                        notifyImageToCamera(listener.getSimpleActivity(), imagePath);
                        UCrop of = UCrop.of(imagePath, Uri.fromFile(getDiskCacheDir()));
                        of.start(listener.getSimpleActivity());
                        break;
                    case UCrop.REQUEST_CROP:
                        listener.onCropSuccess(UCrop.getOutput(data));
                        break;
                }
                break;
        }
    }

    private File getDiskCacheDir() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = listener.getSimpleActivity().getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            } else {
                cachePath = listener.getSimpleActivity().getCacheDir().getPath();
            }
        } else {
            cachePath = listener.getSimpleActivity().getCacheDir().getPath();
        }
        return new File(cachePath, imageName());
    }

    private void notifyImageToCamera(Context context, Uri uri) {
        try {
            File file = new File(uri.getPath());
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }

    private String imageName() {
        return System.currentTimeMillis() + ".jpg";
    }


    public interface RxGalleryFinalCropListener {

        @NonNull
        Activity getSimpleActivity();


        /**
         * 裁剪被取消
         */
        void onCropCancel();

        /**
         * 裁剪成功
         *
         * @param uri 裁剪的 Uri , 有可能会为Null
         */
        void onCropSuccess(@Nullable Uri uri);


        /**
         * 裁剪失败
         *
         * @param errorMessage 错误信息
         */
        void onCropError(@NonNull String errorMessage);

    }
}
