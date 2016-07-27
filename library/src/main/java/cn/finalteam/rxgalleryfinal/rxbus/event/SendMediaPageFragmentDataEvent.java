package cn.finalteam.rxgalleryfinal.rxbus.event;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/27 下午11:14
 */
public class SendMediaPageFragmentDataEvent {
    private List<MediaBean> mediaBeanList;

    public SendMediaPageFragmentDataEvent(List<MediaBean> mediaBeanList){
        this.mediaBeanList = mediaBeanList;
    }

    public List<MediaBean> getMediaBeanList() {
        return mediaBeanList;
    }
}
