package cn.finalteam.rxgalleryfinal.utils;

/**
 * Desction:支持的Media类型
 * Author:pengjianbo
 * Date:16/5/5 下午5:03
 */
public enum  MediaType {
    JPG,PNG,GIF,MP4;

    public boolean hasVideo() {
        return this == MP4;
    }
}
