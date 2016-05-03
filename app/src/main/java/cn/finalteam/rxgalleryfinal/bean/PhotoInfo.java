package cn.finalteam.rxgalleryfinal.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2016/05/03
 * Description:
 */
public class PhotoInfo implements Serializable {
    private int photoId;
    private String photoPath;
    private int width;
    private int height;

    public PhotoInfo(int photoId, String photoPath, int width, int height) {
        this.photoId = photoId;
        this.photoPath = photoPath;
        this.width = width;
        this.height = height;
    }

    public PhotoInfo() {
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof PhotoInfo)) {
            return false;
        }
        PhotoInfo info = (PhotoInfo) o;
        if (info == null) {
            return false;
        }

        return TextUtils.equals(info.getPhotoPath(), getPhotoPath());
    }
}
