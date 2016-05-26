package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;

import java.io.File;
import java.util.List;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/5 下午10:20
 */
public class ThumbnailsUtils {

    public static void createThumbnailsTask(Configuration configuration) {
        boolean isImage = configuration.isImage();
        Context context = configuration.getContext();
        File storeFile = StorageUtils.getCacheDirectory(context);

        Observable.create((Observable.OnSubscribe<List<MediaBean>>) subscriber -> {
            int page = 1;
            final int limit = 10;
            while (true){
                List<MediaBean> mediaBeanList = null;
                if(isImage) {
                    mediaBeanList = MediaUtils.getMediaWithImageList(context, page, limit);
                } else {
                    mediaBeanList = MediaUtils.getMediaWithVideoList(context, page, limit);
                }

                for (MediaBean mediaBean: mediaBeanList) {
                    //创建缩略图
                    //1、先判断缩略图是否存在
                    BitmapUtils.createThumbnails(storeFile.getAbsolutePath(), mediaBean.getOriginalPath());
                }

                subscriber.onNext(mediaBeanList);

                if(mediaBeanList.size() == 0) {
                    subscriber.onCompleted();
                    break;
                } else {
                    page++;
                }
            }
        })
        .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<List<MediaBean>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(List<MediaBean> mediaBeenList) {}
                });
    }


}
