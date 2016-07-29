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
    private int position;

    public SendMediaPageFragmentDataEvent(List<MediaBean> mediaBeanList, int position){
        this.mediaBeanList = mediaBeanList;
        this.position = position;
    }

    public List<MediaBean> getMediaBeanList() {
        return mediaBeanList;
    }

    public int getPosition() {
        return position;
    }
}
