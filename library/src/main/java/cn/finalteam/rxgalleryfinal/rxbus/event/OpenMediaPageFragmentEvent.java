package cn.finalteam.rxgalleryfinal.rxbus.event;

import java.util.ArrayList;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/27 下午11:14
 */
public class OpenMediaPageFragmentEvent {
    private ArrayList<MediaBean> mediaBeanList;
    private int position;

    public OpenMediaPageFragmentEvent(ArrayList<MediaBean> mediaBeanList, int position){
        this.mediaBeanList = mediaBeanList;
        this.position = position;
    }

    public ArrayList<MediaBean> getMediaBeanList() {
        return mediaBeanList;
    }

    public int getPosition() {
        return position;
    }
}
