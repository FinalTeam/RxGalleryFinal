package cn.finalteam.rxgalleryfinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.R;
import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:媒体获取工具
 * Author:pengjianbo  Dujinyang
 * Date:16/5/4 下午4:11
 */
public class MediaUtils {

    public static List<MediaBean> getMediaWithImageList(Context context, int page, int limit) {
        return getMediaWithImageList(context, String.valueOf(Integer.MIN_VALUE), page, limit);
    }

    /**
     * 从数据库中读取图片
     */
    public static List<MediaBean> getMediaWithImageList(Context context, String bucketId, int page, int limit) {
        int offset = (page - 1) * limit;
        List<MediaBean> mediaBeanList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.equals(bucketId, String.valueOf(Integer.MIN_VALUE))) {
            selection = MediaStore.Images.Media.BUCKET_ID + "=?";
            selectionArgs = new String[]{bucketId};
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), selection,
                selectionArgs, MediaStore.Images.Media.DATE_ADDED + " DESC LIMIT " + limit + " OFFSET " + offset);
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    MediaBean mediaBean = parseImageCursorAndCreateThumImage(context, cursor);
                    if (mediaBean != null) {
                        mediaBeanList.add(mediaBean);
                    }
                } while (cursor.moveToNext());
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaBeanList;
    }

    public static List<MediaBean> getMediaWithVideoList(Context context, int page, int limit) {
        return getMediaWithVideoList(context, page, limit);
    }

    /**
     * 从数据库中读取视频
     */
    public static List<MediaBean> getMediaWithVideoList(Context context, String bucketId, int page, int limit) {
        int offset = (page - 1) * limit;
        List<MediaBean> mediaBeanList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Video.Media._ID);
        projection.add(MediaStore.Video.Media.TITLE);
        projection.add(MediaStore.Video.Media.DATA);
        projection.add(MediaStore.Video.Media.BUCKET_ID);
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Video.Media.MIME_TYPE);
        projection.add(MediaStore.Video.Media.DATE_ADDED);
        projection.add(MediaStore.Video.Media.DATE_MODIFIED);
        projection.add(MediaStore.Video.Media.LATITUDE);
        projection.add(MediaStore.Video.Media.LONGITUDE);
        projection.add(MediaStore.Video.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Video.Media.WIDTH);
            projection.add(MediaStore.Video.Media.HEIGHT);
        }
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.equals(bucketId, String.valueOf(Integer.MIN_VALUE))) {
            selection = MediaStore.Video.Media.BUCKET_ID + "=?";
            selectionArgs = new String[]{bucketId};
        }

        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), selection,
                selectionArgs, MediaStore.Video.Media.DATE_ADDED + " DESC LIMIT " + limit + " OFFSET " + offset);
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToFirst();
                do {
                    MediaBean mediaBean = parseVideoCursorAndCreateThumImage(context, cursor);
                    mediaBeanList.add(mediaBean);
                } while (cursor.moveToNext());
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaBeanList;
    }

    /**
     * 根据原图获取图片相关信息
     */
    public static MediaBean getMediaBeanWithImage(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath}, null);
        MediaBean mediaBean = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mediaBean = parseImageCursorAndCreateThumImage(context, cursor);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaBean;
    }

    /**
     * 根据地址获取视频相关信息
     */
    public static MediaBean getMediaBeanWithVideo(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Video.Media._ID);
        projection.add(MediaStore.Video.Media.TITLE);
        projection.add(MediaStore.Video.Media.DATA);
        projection.add(MediaStore.Video.Media.BUCKET_ID);
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Video.Media.MIME_TYPE);
        projection.add(MediaStore.Video.Media.DATE_ADDED);
        projection.add(MediaStore.Video.Media.DATE_MODIFIED);
        projection.add(MediaStore.Video.Media.LATITUDE);
        projection.add(MediaStore.Video.Media.LONGITUDE);
        projection.add(MediaStore.Video.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Video.Media.WIDTH);
            projection.add(MediaStore.Video.Media.HEIGHT);
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection.toArray(new String[projection.size()]),
                MediaStore.Images.Media.DATA + "=?",
                new String[]{originalPath}, null);
        MediaBean mediaBean = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            mediaBean = parseVideoCursorAndCreateThumImage(context, cursor);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return mediaBean;
    }


    /**
     * 解析图片cursor并且创建缩略图
     * <p>
     * update 17.07.23 log
     * <p>
     * 判断图片 Size ，如果小于等于0则返回 Null，避免出现 No such file or directory
     */
    @Nullable
    private static MediaBean parseImageCursorAndCreateThumImage(Context context, Cursor cursor) {
        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        if (TextUtils.isEmpty(originalPath) || size <= 0 || !new File(originalPath).exists()) {
            return null;
        }

        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

        MediaBean mediaBean = new MediaBean();
        mediaBean.setId(id);
        mediaBean.setTitle(title);
        mediaBean.setOriginalPath(originalPath);
        mediaBean.setBucketId(bucketId);
        mediaBean.setBucketDisplayName(bucketDisplayName);
        mediaBean.setMimeType(mimeType);
        mediaBean.setCreateDate(createDate);
        mediaBean.setModifiedDate(modifiedDate);
        mediaBean.setThumbnailBigPath(createThumbnailBigFileName(context, originalPath).getAbsolutePath());
        mediaBean.setThumbnailSmallPath(createThumbnailSmallFileName(context, originalPath).getAbsolutePath());
        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                Logger.e(e);
            }
        }
        mediaBean.setWidth(width);
        mediaBean.setHeight(height);
        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        mediaBean.setLatitude(latitude);
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        mediaBean.setLongitude(longitude);
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
        mediaBean.setOrientation(orientation);
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        mediaBean.setLength(length);

        return mediaBean;
    }

    /**
     * 解析视频cursor并且创建缩略图
     */
    private static MediaBean parseVideoCursorAndCreateThumImage(Context context, Cursor cursor) {
        MediaBean mediaBean = new MediaBean();
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
        mediaBean.setId(id);
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        mediaBean.setTitle(title);
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        mediaBean.setOriginalPath(originalPath);
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
        mediaBean.setBucketId(bucketId);
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
        mediaBean.setBucketDisplayName(bucketDisplayName);
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
        mediaBean.setMimeType(mimeType);
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
        mediaBean.setCreateDate(createDate);
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
        mediaBean.setModifiedDate(modifiedDate);
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        mediaBean.setLength(length);

        //创建缩略图文件
        mediaBean.setThumbnailBigPath(createThumbnailBigFileName(context, originalPath).getAbsolutePath());
        mediaBean.setThumbnailSmallPath(createThumbnailSmallFileName(context, originalPath).getAbsolutePath());

        int width = 0, height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                Logger.e(e);
            }
        }
        mediaBean.setWidth(width);
        mediaBean.setHeight(height);

        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LATITUDE));
        mediaBean.setLatitude(latitude);
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
        mediaBean.setLongitude(longitude);
        return mediaBean;
    }

    public static File createThumbnailBigFileName(Context context, String originalPath) {
        File storeFile = StorageUtils.getCacheDirectory(context);
        return new File(storeFile, "big_" + FilenameUtils.getName(originalPath));
    }

    public static File createThumbnailSmallFileName(Context context, String originalPath) {
        File storeFile = StorageUtils.getCacheDirectory(context);
        return new File(storeFile, "small_" + FilenameUtils.getName(originalPath));
    }

    /**
     * 获取所有的图片文件夹
     */
    public static List<BucketBean> getAllBucketByImage(Context context) {
        return getAllBucket(context, true);
    }

    /**
     * 获取所以视频文件夹
     */
    public static List<BucketBean> getAllBucketByVideo(Context context) {
        return getAllBucket(context, false);
    }

    /**
     * 获取所有的问media文件夹
     */
    public static List<BucketBean> getAllBucket(Context context, boolean isImage) {
        List<BucketBean> bucketBeenList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection;
        if (isImage) {
            projection = new String[]{
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.ORIENTATION,
            };
        } else {
            projection = new String[]{
                    MediaStore.Video.Media.BUCKET_ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            };
        }
        BucketBean allMediaBucket = new BucketBean();
        allMediaBucket.setBucketId(String.valueOf(Integer.MIN_VALUE));
        Uri uri;
        if (isImage) {
            allMediaBucket.setBucketName(context.getString(R.string.gallery_all_image));
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            allMediaBucket.setBucketName(context.getString(R.string.gallery_all_video));
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        bucketBeenList.add(allMediaBucket);
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, projection, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC");
        } catch (Exception e) {
            Logger.e(e);
        }

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                BucketBean bucketBean = new BucketBean();
                String bucketId;
                String bucketKey;
                String cover;
                if (isImage) {
                    bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                    bucketBean.setBucketId(bucketId);
                    String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    bucketBean.setBucketName(bucketDisplayName);
                    bucketKey = MediaStore.Images.Media.BUCKET_ID;
                    cover = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
                    bucketBean.setOrientation(orientation);
                } else {
                    bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
                    bucketBean.setBucketId(bucketId);
                    String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                    bucketBean.setBucketName(bucketDisplayName);
                    bucketKey = MediaStore.Video.Media.BUCKET_ID;
                    cover = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                }
                if (TextUtils.isEmpty(allMediaBucket.getCover())) {
                    allMediaBucket.setCover(cover);
                }
                if (bucketBeenList.contains(bucketBean)) {
                    continue;
                }
                //获取数量
                Cursor c = contentResolver.query(uri, projection, bucketKey + "=?", new String[]{bucketId}, null);
                if (c != null && c.getCount() > 0) {
                    bucketBean.setImageCount(c.getCount());
                }
                bucketBean.setCover(cover);
                if (c != null && !c.isClosed()) {
                    c.close();
                }
                bucketBeenList.add(bucketBean);
            } while (cursor.moveToNext());
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return bucketBeenList;
    }
}
