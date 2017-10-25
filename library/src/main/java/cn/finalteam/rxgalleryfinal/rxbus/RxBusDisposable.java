package cn.finalteam.rxgalleryfinal.rxbus;

import cn.finalteam.rxgalleryfinal.utils.Logger;
import io.reactivex.observers.DisposableObserver;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/22 下午2:40
 */
public abstract class RxBusDisposable<T> extends DisposableObserver<T> {

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e.getMessage());
    }

    protected abstract void onEvent(T t) throws Exception;

}