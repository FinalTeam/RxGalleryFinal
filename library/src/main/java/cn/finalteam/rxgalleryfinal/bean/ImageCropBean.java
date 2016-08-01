package cn.finalteam.rxgalleryfinal.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/8/1 下午10:27
 */
public class ImageCropBean extends MediaBean implements Parcelable {
    private String cropPath;
    private float aspectRatio;

    public ImageCropBean(){}

    protected ImageCropBean(Parcel in) {
        super(in);
        cropPath = in.readString();
        aspectRatio = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(cropPath);
        dest.writeFloat(aspectRatio);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageCropBean> CREATOR = new Creator<ImageCropBean>() {
        @Override
        public ImageCropBean createFromParcel(Parcel in) {
            return new ImageCropBean(in);
        }

        @Override
        public ImageCropBean[] newArray(int size) {
            return new ImageCropBean[size];
        }
    };

    public String getCropPath() {
        return cropPath;
    }

    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void copyMediaBean(MediaBean mediaBean){
        if(mediaBean != null){
            setId(mediaBean.getId());
            setTitle(mediaBean.getTitle());
            setOriginalPath(mediaBean.getOriginalPath());
            setCreateDate(mediaBean.getCreateDate());
            setModifiedDate(mediaBean.getModifiedDate());
            setMimeType(mediaBean.getMimeType());
            setBucketId(mediaBean.getBucketId());
            setBucketDisplayName(mediaBean.getBucketDisplayName());
            setThumbnailSmallPath(mediaBean.getThumbnailSmallPath());
            setThumbnailBigPath(mediaBean.getThumbnailBigPath());
        }
    }
}
