package cn.finalteam.rxgalleryfinal.di.module;

import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import cn.finalteam.rxgalleryfinal.ui.fragment.ImageGridFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.MediaPageFragment;
import cn.finalteam.rxgalleryfinal.ui.fragment.VideoGridFragment;
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
    public ImageGridFragment provideImageGridFragment(){
        return ImageGridFragment.newInstance();
    }

    @Provides
    public VideoGridFragment provideVideoGridFragment(){
        return VideoGridFragment.newInstance();
    }

    @Provides
    public MediaPageFragment provideMediaPageFragment(){
        return MediaPageFragment.newInstance();
    }

}
