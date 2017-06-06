package cn.finalteam.rxgalleryfinal.utils;

import java.io.Serializable;

/**
 * Desction:支持的Media类型
 * Author:pengjianbo  Dujinyang
 * Date:16/5/5 下午5:03
 */
public enum MediaType implements Serializable {
    JPG, PNG, WEBP, GIF, MP4;

    public boolean hasVideo() {
        return this == MP4;
    }
}
