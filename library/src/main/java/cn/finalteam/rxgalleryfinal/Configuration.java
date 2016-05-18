package cn.finalteam.rxgalleryfinal;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.utils.MediaType;

/**
 * Desction:配置信息
 * Author:pengjianbo
 * Date:16/5/7 下午3:58
 */
public class Configuration {

    protected Configuration() {
    }

    private boolean image = true;
    private Context context;
    private MediaType []filterMimes;
    private List<MediaBean> selectedList;
    private boolean radio;
    private int maxSize;

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MediaType[] getFilterMimes() {
        return filterMimes;
    }

    public void setFilterMimes(MediaType[] filterMimes) {
        this.filterMimes = filterMimes;
    }

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean radio) {
        this.radio = radio;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public List<MediaBean> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(List<MediaBean> selectedList) {
        this.selectedList = selectedList;
    }
}
