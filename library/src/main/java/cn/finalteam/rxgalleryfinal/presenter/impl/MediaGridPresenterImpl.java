package cn.finalteam.rxgalleryfinal.presenter.impl;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.BucketBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.interactor.MediaBucketFactoryInteractor;
import cn.finalteam.rxgalleryfinal.interactor.MediaSrcFactoryInteractor;
import cn.finalteam.rxgalleryfinal.interactor.impl.MediaBucketFactoryInteractorImpl;
import cn.finalteam.rxgalleryfinal.interactor.impl.MediaSrcFactoryInteractorImpl;
import cn.finalteam.rxgalleryfinal.presenter.MediaGridPresenter;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午10:58
 */
public class MediaGridPresenterImpl implements MediaGridPresenter, MediaSrcFactoryInteractor.OnGenerateMediaListener,
        MediaBucketFactoryInteractor.OnGenerateBucketListener{

    MediaSrcFactoryInteractor mediaSrcFactoryInteractor;
    MediaBucketFactoryInteractor mediaBucketFactoryInteractor;

    Context context;
    MediaGridView mediaGridView;

    public MediaGridPresenterImpl(Context context, boolean isImage) {
        this.context = context;
        this.mediaSrcFactoryInteractor = new MediaSrcFactoryInteractorImpl(context, isImage, this);
        this.mediaBucketFactoryInteractor = new MediaBucketFactoryInteractorImpl(context, isImage, this);
    }

    /**
     * 设置MVP view(操作UI接口)
     * @param mediaGridView
     */
    @Override
    public void setMediaGridView(MediaGridView mediaGridView) {
        this.mediaGridView = mediaGridView;
    }

    /**
     * 分页获取media
     * @param bucketId
     * @param pageSize
     * @param currentOffset
     */
    @Override
    public void getMediaList(String bucketId, int pageSize, int currentOffset) {
        mediaSrcFactoryInteractor.generateMeidas(bucketId, pageSize, currentOffset);
    }

    @Override
    public void getBucketList() {
        mediaBucketFactoryInteractor.generateBuckets();
    }

    /**
     * Media获取事件回调
     * @param pageSize
     * @param currentOffset
     * @param list
     */
    @Override
    public void onFinished(String bucketId, int pageSize, int currentOffset, List<MediaBean> list) {
        mediaGridView.onRequestMediaCallback(list);
    }

    /**
     * BUCKET获取事件回调
     * @param list
     */
    @Override
    public void onFinished(List<BucketBean> list) {
        mediaGridView.onRequestBucketCallback(list);
    }
}
