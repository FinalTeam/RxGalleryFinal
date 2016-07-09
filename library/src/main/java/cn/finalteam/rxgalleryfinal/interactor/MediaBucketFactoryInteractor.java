package cn.finalteam.rxgalleryfinal.interactor;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.BucketBean;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/4 下午8:24
 */
public interface MediaBucketFactoryInteractor {

    interface OnGenerateBucketListener {
        void onFinished(List<BucketBean> list);
    }

    void generateBuckets();
}
