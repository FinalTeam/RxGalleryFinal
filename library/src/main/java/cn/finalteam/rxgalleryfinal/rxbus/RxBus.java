package cn.finalteam.rxgalleryfinal.rxbus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/22 下午2:40
 */
public class RxBus {

    private static volatile RxBus mInstance;
    private final Subject<Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;

    private final CompositeDisposable mDisposable;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
        mDisposable = new CompositeDisposable();
        mStickyEventMap = new HashMap<>();
    }

    public static RxBus getDefault() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 判断是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        mInstance = null;
    }


    /**
     * 是否被取消订阅
     */
    public boolean isUnsubscribed() {
        return mDisposable.isDisposed();
    }

    /**
     * 添加订阅
     */
    public void add(Disposable s) {
        if (s != null) {
            mDisposable.add(s);
        }
    }

    /**
     * 移除订阅
     */
    public void remove(Disposable s) {
        if (s != null) {
            mDisposable.remove(s);
        }
    }

    /**
     * 清除所有订阅
     */
    public void clear() {
        mDisposable.clear();
    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        mDisposable.dispose();
    }

    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return Observable.merge(observable, Observable.create(subscriber -> subscriber.onNext(eventType.cast(event))));
            } else {
                return observable;
            }
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
