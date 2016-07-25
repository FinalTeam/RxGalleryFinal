package cn.finalteam.rxgalleryfinal.di.module;

import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaGridFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPageFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPreviewFragment;
import dagger.Module;
import dagger.Provides;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/15 上午1:19
 */
@Module
@RxGalleryFinalScope
public class ActivityFragmentModule {

    @Provides
    public MediaGridFragment provideImageGridFragment(){
        return MediaGridFragment.newInstance();
    }

    @Provides
    public MediaPageFragment provideMediaPageFragment(){
        return MediaPageFragment.newInstance();
    }

    @Provides
    MediaPreviewFragment provideMediaPreviewFragment(){
        return MediaPreviewFragment.newInstance();
    }

}
