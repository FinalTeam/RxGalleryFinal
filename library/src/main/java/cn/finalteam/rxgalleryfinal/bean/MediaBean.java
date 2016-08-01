package cn.finalteam.rxgalleryfinal.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Desction:Media Bean
 * Author:pengjianbo
 * Date:16/5/4 下午4:14
 */
public class MediaBean implements Parcelable {

    //图片ID
    private long id;

    private String title;
    //图片、视频源地址
    private String originalPath;
    //图片、视频创建时间
    private long createDate;
    //图片、视频最后修改时间
    private long modifiedDate;
    //媒体类型
    private String mimeType;

    //文件夹相关
    private String bucketId;
    private String bucketDisplayName;

    //大缩略图
    private String thumbnailBigPath;
    //小缩略图
    private String thumbnailSmallPath;

    public MediaBean(){}

    protected MediaBean(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalPath = in.readString();
        createDate = in.readLong();
        modifiedDate = in.readLong();
        mimeType = in.readString();
        bucketId = in.readString();
        bucketDisplayName = in.readString();
        thumbnailBigPath = in.readString();
        thumbnailSmallPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalPath);
        dest.writeLong(createDate);
        dest.writeLong(modifiedDate);
        dest.writeString(mimeType);
        dest.writeString(bucketId);
        dest.writeString(bucketDisplayName);
        dest.writeString(thumbnailBigPath);
        dest.writeString(thumbnailSmallPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaBean> CREATOR = new Creator<MediaBean>() {
        @Override
        public MediaBean createFromParcel(Parcel in) {
            return new MediaBean(in);
        }

        @Override
        public MediaBean[] newArray(int size) {
            return new MediaBean[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof MediaBean)){
            return false;
        }

        MediaBean bean = (MediaBean) obj;
        if(bean == null){
            return false;
        }

        return bean.getId() == getId();
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalPath='" + originalPath + '\'' +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                ", mimeType='" + mimeType + '\'' +
                ", bucketId='" + bucketId + '\'' +
                ", bucketDisplayName='" + bucketDisplayName + '\'' +
                ", thumbnailBigPath='" + thumbnailBigPath + '\'' +
                ", thumbnailSmallPath='" + thumbnailSmallPath + '\'' +
                '}';
    }
}
