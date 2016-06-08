package cn.finalteam.rxgalleryfinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:媒体获取工具
 * Author:pengjianbo
 * Date:16/5/4 下午4:11
 */
public class MediaUtils {

    /**
     * 从数据库中读取图片
     * @param context
     * @param page
     * @param limit
     * @return
     */
    public static List<MediaBean> getMediaWithImageList(Context context, int page, int limit) {
        int offset = (page -1) * limit;
        List<MediaBean> mediaBeanList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_ADDED,//创建时间
                MediaStore.Images.Media.DATE_MODIFIED//最后修改时间
        };
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Images.Media.DATE_ADDED +" DESC LIMIT " + limit +" OFFSET " + offset);
        if(cursor != null) {
            int count = cursor.getCount();
            if(count > 0) {
                cursor.moveToFirst();
                do {
                    MediaBean mediaBean = parseImageCursorAndCreateThumImage(context, cursor);
                    mediaBeanList.add(mediaBean);
                } while (cursor.moveToNext());
            }
        }

        return mediaBeanList;
    }

    /**
     * 从数据库中读取视频
     * @param context
     * @param page
     * @param limit
     * @return
     */
    public static List<MediaBean> getMediaWithVideoList(Context context, int page, int limit) {
        int offset = (page -1) * limit;
        List<MediaBean> mediaBeanList = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED,//创建时间
                MediaStore.Video.Media.DATE_MODIFIED//最后修改时间
        };
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                null, MediaStore.Video.Media.DATE_ADDED +" DESC LIMIT " + limit +" OFFSET " + offset);
        if(cursor != null) {
            int count = cursor.getCount();
            if(count > 0) {
                cursor.moveToFirst();
                do {
                    MediaBean mediaBean = parseVideoCursorAndCreateThumImage(context, cursor);
                    mediaBeanList.add(mediaBean);
                } while (cursor.moveToNext());
            }
        }

        return mediaBeanList;
    }

    /**
     * 根据原图获取图片相关信息
     * @param context
     * @param originalPath
     * @return
     */
    public static MediaBean getMediaBeanWithImage(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED,//创建时间
                MediaStore.Video.Media.DATE_MODIFIED//最后修改时间
        };
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Video.Media.DATA +"=?",
                new String[]{originalPath}, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return parseImageCursorAndCreateThumImage(context, cursor);
        }
        return null;
    }

    /**
     * 解析图片cursor并且创建缩略图
     * @param context
     * @param cursor
     * @return
     */
    private static MediaBean parseImageCursorAndCreateThumImage(Context context, Cursor cursor) {
        MediaBean mediaBean = new MediaBean();
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        mediaBean.setId(id);
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        mediaBean.setTitle(title);
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        mediaBean.setOriginalPath(originalPath);
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        mediaBean.setBucketId(bucketId);
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        mediaBean.setBucketDisplayName(bucketDisplayName);
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        mediaBean.setMimeType(mimeType);
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        mediaBean.setCreateDate(createDate);
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
        mediaBean.setModifiedDate(modifiedDate);


        File storeFile = StorageUtils.getCacheDirectory(context);
        File bigThumFile = new File(storeFile, "big_" + FilenameUtils.getName(originalPath));
        File smallThumFile = new File(storeFile, "small_" + FilenameUtils.getName(originalPath));
        if(!smallThumFile.exists()){
            String thum = BitmapUtils.getThumbnailSmallPath(storeFile.getAbsolutePath(), originalPath);
            mediaBean.setThumbnailSmallPath(thum);
        }else{
            mediaBean.setThumbnailSmallPath(smallThumFile.getAbsolutePath());
        }
        if(!bigThumFile.exists()){
            String thum = BitmapUtils.getThumbnailBigPath(storeFile.getAbsolutePath(), originalPath);
            mediaBean.setThumbnailBigPath(thum);
        } else {
            mediaBean.setThumbnailBigPath(bigThumFile.getAbsolutePath());
        }

        mediaBean.setThumbnailSmallPath(bigThumFile.getAbsolutePath());
        return mediaBean;
    }

    /**
     * 解析视频cursor并且创建缩略图
     * @param context
     * @param cursor
     * @return
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

        //获取缩略图
        File storeFile = StorageUtils.getCacheDirectory(context);
        File bigThumFile = new File(storeFile, "big_" + FilenameUtils.getName(originalPath));
        File smallThumFile = new File(storeFile, "small_" + FilenameUtils.getName(originalPath));
        if(!smallThumFile.exists()){
            String thum = BitmapUtils.getVideoThumbnailSmallPath(storeFile.getAbsolutePath(), originalPath);
            mediaBean.setThumbnailSmallPath(thum);
        }else{
            mediaBean.setThumbnailSmallPath(smallThumFile.getAbsolutePath());
        }
        if(!bigThumFile.exists()){
            String thum = BitmapUtils.getVideoThumbnailBigPath(storeFile.getAbsolutePath(), originalPath);
            mediaBean.setThumbnailBigPath(thum);
        } else {
            mediaBean.setThumbnailBigPath(bigThumFile.getAbsolutePath());
        }

        return mediaBean;

    }

}
