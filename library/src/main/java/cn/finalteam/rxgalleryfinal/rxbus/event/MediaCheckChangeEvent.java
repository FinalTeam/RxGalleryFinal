package cn.finalteam.rxgalleryfinal.rxbus.event;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/24 下午11:47
 */
public class MediaCheckChangeEvent {

    private MediaBean mediaBean;

    public MediaCheckChangeEvent(MediaBean mediaBean){
        this.mediaBean = mediaBean;
    }

    public MediaBean getMediaBean() {
        return this.mediaBean;
    }
}
