package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;

import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.util.BitmapLoadUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Desction:Bitmap处理工具类,图片压缩、裁剪、选择、存储
 * Author:pengjianbo  Dujinyang
 * Date:16/5/4 下午5:03
 */
public class BitmapUtils {

    private final static int THUMBNAIL_BIG = 1;
    private final static int THUMBNAIL_SMALL = 2;

    public static void createVideoThumbnailBigPath(String thumbnailSaveDir, String originalPath) {
        createVideoThumbnail(thumbnailSaveDir, originalPath, THUMBNAIL_BIG);
    }

    public static void createVideoThumbnailSmallPath(String thumbnailSaveDir, String originalPath) {
        createVideoThumbnail(thumbnailSaveDir, originalPath, THUMBNAIL_SMALL);
    }

    /**
     * 创建视频缩略图
     */
    public static void createVideoThumbnail(String thumbnailSaveDir, String originalPath, int scale) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(originalPath, MediaStore.Video.Thumbnails.MINI_KIND);
        if (bitmap == null) {
            return;
        }
        int originalImageWidth = bitmap.getWidth();
        int originalImageHeight = bitmap.getHeight();
        int maxValue = Math.max(originalImageWidth, originalImageHeight);
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        File targetFile;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (maxValue > 3000) {
                options.inSampleSize = scale * 5;
            } else if (maxValue > 2000 && maxValue <= 3000) {
                options.inSampleSize = scale * 4;
            } else if (maxValue > 1500 && maxValue <= 2000) {
                options.inSampleSize = (int) (scale * 2.5);
            } else if (maxValue > 1000 && maxValue <= 1500) {
                options.inSampleSize = (int) (scale * 1.3);
//            } else if (maxValue > 400 && maxValue <= 1000) {
//                options.inSampleSize = scale * 2;
            } else {
                options.inSampleSize = scale;
            }
            options.inJustDecodeBounds = false;

            //4、图片方向纠正和压缩(生成缩略图)
            bufferedInputStream = new BufferedInputStream(new FileInputStream(originalPath));
            Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream, null, options);
            bufferedInputStream.close();
            bitmap.recycle();
            if (bm == null) {
                return;
            }
            bitmap = bm;

            String scaleStr = (scale == THUMBNAIL_BIG ? "big" : "small");

            String extension = FilenameUtils.getExtension(originalPath);
            File original = new File(originalPath);
            targetFile = new File(thumbnailSaveDir, scaleStr + "_" + original.getName().replace(extension, "jpg"));

            fileOutputStream = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            Logger.e(e);
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }

            IOUtils.close(bufferedInputStream);
            IOUtils.flush(fileOutputStream);
            IOUtils.close(fileOutputStream);
        }
    }

    /**
     * 创建大缩略图
     *
     * @param targetFile   保存目标文件
     * @param originalPath 图片地址
     */
    public static void createThumbnailBig(File targetFile, String originalPath) {
        compressAndSaveImage(targetFile, originalPath, THUMBNAIL_BIG);
    }

    /**
     * 创建小缩略图
     *
     * @param targetFile   保存目标文件
     * @param originalPath 图片地址
     */
    public static void createThumbnailSmall(File targetFile, String originalPath) {
        compressAndSaveImage(targetFile, originalPath, THUMBNAIL_SMALL);
    }

    /**
     * 图片压缩并且存储
     *
     * @param targetFile   保存目标文件
     * @param originalPath 图片地址
     * @param scale        图片缩放值
     */
    public static void compressAndSaveImage(File targetFile, String originalPath, int scale) {

        Bitmap bitmap = null;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //1、得到图片的宽、高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bufferedInputStream = new BufferedInputStream(new FileInputStream(originalPath));
            bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
            if (bitmap != null) {
                bitmap.recycle();
            }
            bufferedInputStream.close();

            int originalImageWidth = options.outWidth;
            int originalImageHeight = options.outHeight;

            //2、获取图片方向
            int orientation = getImageOrientation(originalPath);
            int rotate = 0;
            switch (orientation) {//判断是否需要旋转
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = -90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            //3、计算图片压缩inSampleSize
            int maxValue = Math.max(originalImageWidth, originalImageHeight);
            if (maxValue > 3000) {
                options.inSampleSize = scale * 5;
            } else if (maxValue > 2000 && maxValue <= 3000) {
                options.inSampleSize = scale * 4;
            } else if (maxValue > 1500 && maxValue <= 2000) {
                options.inSampleSize = (int) (scale * 2.5);
            } else if (maxValue > 1000 && maxValue <= 1500) {
                options.inSampleSize = (int) (scale * 1.3);
//            } else if (maxValue > 400 && maxValue <= 1000) {
//                options.inSampleSize = scale * 2;
            } else {
                options.inSampleSize = scale;
            }
            options.inJustDecodeBounds = false;

            //4、图片方向纠正和压缩(生成缩略图)
            bufferedInputStream = new BufferedInputStream(new FileInputStream(originalPath));
            bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
            bufferedInputStream.close();

            if (bitmap == null) {
                return;
            }
            String extension = FilenameUtils.getExtension(originalPath);

            targetFile.getParentFile().mkdirs();

            fileOutputStream = new FileOutputStream(targetFile);
            if (rotate != 0) {
                Matrix matrix = new Matrix();
                matrix.setRotate(rotate);
                Bitmap bitmapOld = bitmap;
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, false);
                bitmapOld.recycle();
            }

            //5、保存图片
            if (TextUtils.equals(extension.toLowerCase(), "jpg")
                    || TextUtils.equals(extension.toLowerCase(), "jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } else if (TextUtils.equals(extension.toLowerCase(), "webp")
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bufferedInputStream);
            IOUtils.flush(fileOutputStream);
            IOUtils.close(fileOutputStream);
            if (bitmap != null && bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 获取一张图片在手机上的方向值
     */
    public static int getImageOrientation(String uri) throws IOException {
        if (!new File(uri).exists()) {
            return 0;
        }
        return new ExifInterface(uri).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    /**
     * Drawable着色工具
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public void decodeBitmapInBackground(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri, BitmapLoadCallback loadCallback) {
        int maxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(context);
        decodeBitmapInBackground(context, uri, outputUri, maxBitmapSize, maxBitmapSize, loadCallback);
    }

    /**
     * 获取图片Bitmap
     */
    public void decodeBitmapInBackground(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri,
                                         int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
        BitmapLoadUtils.decodeBitmapInBackground(context, uri, outputUri, requiredWidth, requiredHeight, loadCallback);
    }

    /**
     * 图片压缩旋转
     */
    public void rotateImage(@NonNull Context context, @NonNull Uri uri, @Nullable Uri outputUri,
                            int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {

    }
}
