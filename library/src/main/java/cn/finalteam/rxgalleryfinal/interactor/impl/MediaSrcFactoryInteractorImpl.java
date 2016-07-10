package cn.finalteam.rxgalleryfinal.interactor.impl;

import android.content.Context;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.interactor.MediaSrcFactoryInteractor;
import cn.finalteam.rxgalleryfinal.utils.MediaUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/14 上午11:08
 */
public class MediaSrcFactoryInteractorImpl implements MediaSrcFactoryInteractor {

    Context context;
    OnGenerateMediaListener onGenerateMediaListener;
    boolean isImage;

    public MediaSrcFactoryInteractorImpl(Context context, boolean isImage, OnGenerateMediaListener onGenerateMediaListener) {
        this.context = context;
        this.isImage = isImage;
        this.onGenerateMediaListener = onGenerateMediaListener;
    }

    @Override
    public void generateMeidas(final String bucketId, final int page, final int limit) {
        Observable.create((Observable.OnSubscribe<List<MediaBean>>) subscriber -> {
            List<MediaBean> mediaBeanList = null;
            if(isImage) {
                mediaBeanList = MediaUtils.getMediaWithImageList(context, bucketId, page, limit);
            } else {
                mediaBeanList = MediaUtils.getMediaWithVideoList(context, bucketId, page, limit);
            }
            subscriber.onNext(mediaBeanList);
            subscriber.onCompleted();
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<MediaBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                onGenerateMediaListener.onFinished(bucketId, page, limit, null);
            }

            @Override
            public void onNext(List<MediaBean> mediaBeenList) {
                onGenerateMediaListener.onFinished(bucketId, page, limit, mediaBeenList);
            }
        });
    }
}
