package cn.finalteam.rxgalleryfinal.di.component;

import android.content.Context;

import javax.inject.Singleton;

import cn.finalteam.rxgalleryfinal.di.module.Configuration;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import dagger.Component;

/**
 * Desction:生产组件
 * Author:pengjianbo
 * Date:16/5/7 下午3:56
 */

@RxGalleryFinalScope
@Singleton
@Component(modules = {Configuration.class})
public interface RxGalleryFinalComponent {

    void inject(RxGalleryFinal rxGalleryFinal);

    //在组件下定义的方法需要在每个Module上实现
    Context getContext();
}
