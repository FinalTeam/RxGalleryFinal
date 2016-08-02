package cn.finalteam.rxgalleryfinal.di.component;

import cn.finalteam.rxgalleryfinal.di.module.ActivityFragmentModule;
import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import cn.finalteam.rxgalleryfinal.ui.activity.MediaActivity;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaGridFragment;
import dagger.Component;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/15 上午1:17
 */
@Component(dependencies = RxGalleryFinalComponent.class, modules = ActivityFragmentModule.class)
@RxGalleryFinalScope
public interface ActivityFragmentComponent {
    void inject(MediaActivity mediaActivity);

    MediaGridFragment provideImageGridFragment();
}
