package cn.finalteam.rxgalleryfinal.bean;

/**
 * Desction:Media Bean
 * Author:pengjianbo
 * Date:16/5/4 下午4:14
 */
public class MediaBean {

    //===================MEDIA TYPE==================
    public static final int IMAGE_JPG = 0X001;
    public static final int IMAGE_PNG = 0X002;
    public static final int IMAGE_GIF = 0X003;

    public static final int VIDEO_MP4 = 0X010;
    //===================MEDIA TYPE==================


    //图片、视频源地址
    private String originalPath;
    //图片、视频父级目录
    private String parentDir;
    //图片、视频创建时间
    private long createDate;
    //大缩略图
    private String thumbnailBigPath;
    //小缩略图
    private String thumbnailSmallPath;
    //媒体类型
    private int mediaType;

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getThumbnailBigPath() {
        return thumbnailBigPath;
    }

    public void setThumbnailBigPath(String thumbnailBigPath) {
        this.thumbnailBigPath = thumbnailBigPath;
    }

    public String getThumbnailSmallPath() {
        return thumbnailSmallPath;
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
}
