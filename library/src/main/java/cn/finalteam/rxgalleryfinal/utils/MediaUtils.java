package cn.finalteam.rxgalleryfinal.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

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

                    mediaBeanList.add(mediaBean);
                } while (cursor.moveToNext());
            }
        }

        return mediaBeanList;
    }

}
