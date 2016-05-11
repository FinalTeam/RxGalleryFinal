package cn.finalteam.rxgalleryfinal;

import cn.finalteam.rxgalleryfinal.di.component.DaggerRxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.component.RxGalleryFinalComponent;
import cn.finalteam.rxgalleryfinal.di.module.Configuration;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/7 下午4:20
 */
public class RxGalleryFinal {

    private RxGalleryFinalComponent mRxGalleryFinalComponent;

    public void init(Configuration.Builder builder){
        mRxGalleryFinalComponent = DaggerRxGalleryFinalComponent.builder()
                .configuration(builder.build())
                .build();
        mRxGalleryFinalComponent.inject(this);
    }


    public RxGalleryFinalComponent getRxGalleryFinalComponent() {
        return mRxGalleryFinalComponent;
    }
}
