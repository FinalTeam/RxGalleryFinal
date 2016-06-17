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
    private AbsImageLoader imageLoader;
    private PauseOnScrollListener pauseOnScrollListener;

    public boolean isImage() {
        return image;
    }

    protected void setImage(boolean image) {
        this.image = image;
    }

    public Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    public MediaType[] getFilterMimes() {
        return filterMimes;
    }

    protected void setFilterMimes(MediaType[] filterMimes) {
        this.filterMimes = filterMimes;
    }

    public List<MediaBean> getSelectedList() {
        return selectedList;
    }

    protected void setSelectedList(List<MediaBean> selectedList) {
        this.selectedList = selectedList;
    }

    public boolean isRadio() {
        return radio;
    }

    protected void setRadio(boolean radio) {
        this.radio = radio;
    }

    public int getMaxSize() {
        return maxSize;
    }

    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public AbsImageLoader getImageLoader() {
        return imageLoader;
    }

    protected void setImageLoader(AbsImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public PauseOnScrollListener getPauseOnScrollListener() {
        return pauseOnScrollListener;
    }

    protected void setPauseOnScrollListener(PauseOnScrollListener pauseOnScrollListener) {
        this.pauseOnScrollListener = pauseOnScrollListener;
    }
}
