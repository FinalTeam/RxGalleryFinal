package cn.finalteam.rxgalleryfinal.rxbus.event;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/8/1 下午10:52
 */
public class ImageMultipleResultEvent implements BaseResultEvent {
    private final List<MediaBean> mediaResultList;

    public ImageMultipleResultEvent(List<MediaBean> list) {
        this.mediaResultList = list;
    }

    public List<MediaBean> getResult() {
        return mediaResultList;
    }
}
