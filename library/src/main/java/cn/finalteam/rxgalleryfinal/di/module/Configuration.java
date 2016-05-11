package cn.finalteam.rxgalleryfinal.di.module;

import android.content.Context;

import javax.inject.Singleton;

import cn.finalteam.rxgalleryfinal.di.scope.RxGalleryFinalScope;
import dagger.Module;
import dagger.Provides;

/**
 * Desction:配置信息
 * Author:pengjianbo
 * Date:16/5/7 下午3:58
 */
@Module
@RxGalleryFinalScope
public class Configuration {

    private Context context;

    public Configuration(Builder builder) {
        context = builder.context;
    }

    /**
     * Configuration builder
     */
    public static class Builder{
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return context;
    }

}
