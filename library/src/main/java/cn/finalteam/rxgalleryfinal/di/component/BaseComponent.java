package cn.finalteam.rxgalleryfinal.di.component;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.module.BaseModule;
import cn.finalteam.rxgalleryfinal.di.scope.BaseScope;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;
import dagger.Component;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/22 上午11:29
 */
@BaseScope
@Component(dependencies = RxGalleryFinalComponent.class, modules = BaseModule.class)
public interface BaseComponent {
    void inject(MediaPreviewFragment mediaPreviewFragment);

    List<MediaBean> provideMediaBeans();
}
