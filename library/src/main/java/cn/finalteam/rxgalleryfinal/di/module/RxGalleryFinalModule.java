package cn.finalteam.rxgalleryfinal.di.module;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import javax.inject.Singleton;

import cn.finalteam.rxgalleryfinal.Configuration;
import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import dagger.Module;
import dagger.Provides;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/15 上午12:03
 */
@Module
@RxGalleryFinalScope
public class RxGalleryFinalModule {

    Configuration configuration;

    public RxGalleryFinalModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Provides
    @Singleton
    public Configuration provideConfiguration() {
        return configuration;
    }

    @Provides
    @Singleton
    public DisplayMetrics provideScreenPix() {
        Context context = configuration.getContext();
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        context.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics;
    }

}
