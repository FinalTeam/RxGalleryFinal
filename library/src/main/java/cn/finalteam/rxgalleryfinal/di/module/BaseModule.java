package cn.finalteam.rxgalleryfinal.di.module;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.di.scope.BaseScope;
import dagger.Module;
import dagger.Provides;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/5/15 上午1:19
 */
@Module
@BaseScope
public class BaseModule {

    @Provides
    public List<MediaBean> provideMediaBeans(){
        return null;
    }
}
