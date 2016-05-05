package cn.finalteam.rxgalleryfinal.utils;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * Desction:Bitmap处理工具类,图片压缩、裁剪、选择、存储
 * Author:pengjianbo
 * Date:16/5/4 下午5:03
 */
public class BitmapUtils {

    private final static int THUMBNAIL_BIG = 1;
    private final static int THUMBNAIL_SMALL = 2;

    /**
     * 创建缩略图
     * @param originalPath
     * @return
     */
    public static String[] createThumbnails(String originalPath) {
        String[] images = new String[2];
        images[0] = getThumbnailBigPath(originalPath);
        images[1] = getThumbnailSmallPath(originalPath);
        return images;
    }

    public static String getThumbnailBigPath(String originalPath) {
        return compressAndSaveImage(originalPath, THUMBNAIL_BIG);
    }

    public static String getThumbnailSmallPath(String originalPath) {
        return compressAndSaveImage(originalPath, THUMBNAIL_SMALL);
    }

    /**
     * 图片压缩并且存储
     * @param uri 图片地址
     * @param scale 图片缩放值
     * @return
     */
    public static String compressAndSaveImage(String uri, int scale) {

        //1、得到图片的宽、高

        //2、获取图片方向

        //3、计算图片压缩inSampleSize

        //4、保存图片

        return null;
    }

    /**
     * 获取一张图片在手机上的方向值
     * @param uri
     * @return
     * @throws IOException
     */
    public static int getImageOrientation(String uri) throws IOException {
        ExifInterface exif = new ExifInterface(uri);
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        return orientation;
    }
}
