package cn.finalteam.rxgalleryfinal.interactor;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Desction:媒体资源工厂
 * Author:pengjianbo
 * Date:16/5/14 上午11:06
 */
public interface MediaSrcFactoryInteractor {

    interface OnGenerateMediaListener {
        void onFinished(int pageSize, int currentOffset, List<MediaBean> list);
    }

    /**
     * 生产资源
     * @param page
     * @param limit
     */
    void generateMeidas(int page, int limit);

}
