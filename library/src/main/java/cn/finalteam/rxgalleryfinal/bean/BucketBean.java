package cn.finalteam.rxgalleryfinal.bean;

import android.text.TextUtils;

/**
 * Desction:文件夹信息
 * Author:pengjianbo
 * Date:16/6/9 下午2:47
 */
public class BucketBean {
    private String bucketId;
    private String bucketName;
    private int imageCount;
    private String cover;
    //图片方向
    private int orientation;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getCover() {
        if(cover == null){
            return "";
        }
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof BucketBean)) {
            return false;
        }
        BucketBean bucketBean = (BucketBean) o;
        return TextUtils.equals(bucketBean.getBucketId(), getBucketId());
    }
}
