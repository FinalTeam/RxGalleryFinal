package cn.finalteam.rxgalleryfinal.presenter.impl;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.interactor.MediaSrcFactoryInteractor;
import cn.finalteam.rxgalleryfinal.interactor.impl.MediaSrcFactoryInteractorImpl;
import cn.finalteam.rxgalleryfinal.presenter.MediaGridPresenter;
import cn.finalteam.rxgalleryfinal.view.MediaGridView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午10:58
 */
public class MediaGridPresenterImpl implements MediaGridPresenter, MediaSrcFactoryInteractor.OnGenerateMediaListener{

    MediaSrcFactoryInteractorImpl mediaSrcFactoryInteractor;

    Context context;
    MediaGridView mediaGridView;

    public MediaGridPresenterImpl(Context context, boolean hasImage) {
        this.context = context;
        this.mediaSrcFactoryInteractor = new MediaSrcFactoryInteractorImpl(context, hasImage, this);
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
     * @param pageSize
     * @param currentOffset
     */
    @Override
    public void getMediaList(int pageSize, int currentOffset) {
        mediaSrcFactoryInteractor.generateMeidas(pageSize, currentOffset);
    }

    /**
     * Media获取事件回调
     * @param pageSize
     * @param currentOffset
     * @param list
     */
    @Override
    public void onFinished(int pageSize, int currentOffset, List<MediaBean> list) {
        mediaGridView.onRequestMediaCallback(list);
    }
}
