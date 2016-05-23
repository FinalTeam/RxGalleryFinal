package cn.finalteam.rxgalleryfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.component.DaggerRxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.RxGalleryFinalModule;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.utils.MediaType;
import cn.finalteam.rxgalleryfinal.utils.ThumbnailsUtils;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 下午4:20
 */
public class RxGalleryFinal {

    private RxGalleryFinalComponent mRxGalleryFinalComponent;

    private RxGalleryFinal(){}

    Configuration configuration = new Configuration();
    static RxGalleryFinal instance;

    public static RxGalleryFinal with(@NonNull Context context) {
        instance = new RxGalleryFinal();
        instance.configuration.setContext(context);
        return instance;
    }

    public RxGalleryFinal image(){
        configuration.setImage(true);
        return this;
    }

    public RxGalleryFinal video() {
        configuration.setImage(false);
        return this;
    }

    public RxGalleryFinal filterMime(MediaType ...mediaTypes) {
        configuration.setFilterMimes(mediaTypes);
        return this;
    }

    public RxGalleryFinal radio(){
        configuration.setRadio(true);
        return this;
    }

    public RxGalleryFinal multiple() {
        configuration.setRadio(false);
        return this;
    }

    public RxGalleryFinal maxSize(int maxSize){
        configuration.setMaxSize(maxSize);
        return this;
    }

    public RxGalleryFinal selectedList(List<MediaBean> list) {
        configuration.setSelectedList(list);
        return this;
    }

    public void openGallery(){
        ThumbnailsUtils.createThumbnailsTask(configuration);
//        execute();
    }

    public void openCamera() {

    }

    private void execute() {
        Context context = configuration.getContext();
        if(context == null) {
            return;
        }
        mRxGalleryFinalComponent = DaggerRxGalleryFinalComponent.builder()
                .rxGalleryFinalModule(new RxGalleryFinalModule(configuration))
                .build();

        mRxGalleryFinalComponent.inject(this);

        Intent intent = new Intent(context, MediaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static RxGalleryFinalComponent getRxGalleryFinalComponent() {
        if(instance == null) {
            return null;
        }
        return instance.mRxGalleryFinalComponent;
    }
}
