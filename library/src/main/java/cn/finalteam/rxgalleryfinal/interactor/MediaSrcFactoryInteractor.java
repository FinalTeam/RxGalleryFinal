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
        void onFinished(String bucketId, int pageSize, int currentOffset, List<MediaBean> list);
    }

    /**
     * 生产资源
     * @param bucketId
     * @param page
     * @param limit
     */
    void generateMeidas(String bucketId, int page, int limit);

}
