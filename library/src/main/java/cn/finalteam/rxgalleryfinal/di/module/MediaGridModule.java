package cn.finalteam.rxgalleryfinal.di.module;

import android.content.Context;

import cn.finalteam.rxgalleryfinal.di.scope.MediaGridScope;
import cn.finalteam.rxgalleryfinal.presenter.impl.MediaGridPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/18 下午5:10
 */
@Module
@MediaGridScope
public class MediaGridModule {

    private Context context;
    private boolean isImage;

    public MediaGridModule(Context context, boolean isImage) {
        this.context = context;
        this.isImage = isImage;
    }

    @Provides
    public MediaGridPresenterImpl provideMediaGridPresenter(){
        return new MediaGridPresenterImpl(context, isImage);
    }

}
