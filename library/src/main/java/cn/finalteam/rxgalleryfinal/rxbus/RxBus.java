package cn.finalteam.rxgalleryfinal.rxbus;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/7/22 下午2:40
 */
public class RxBus {

    private static volatile RxBus mInstance;
    private final Subject<Object, Object> mBus;
    private final Map<Class<?>, Object> mStickyEventMap;

    private final CompositeSubscription mSubscriptions;

    public RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
        mSubscriptions = new CompositeSubscription();
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
     * @return
     */
    public boolean isUnsubscribed() {
        return mSubscriptions.isUnsubscribed();
    }

    /**
     * 添加订阅
     * @param s
     */
    public void add(Subscription s) {
        if (s != null) {
            mSubscriptions.add(s);
        }
    }

    /**
     * 移除订阅
     * @param s
     */
    public void remove(Subscription s) {
        if (s != null) {
            mSubscriptions.remove(s);
        }
    }

    /**
     * 清除所有订阅
     */
    public void clear() {
        mSubscriptions.clear();
    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }

    /**
     * 判断是否有订阅者
     * @return
     */
    public boolean hasSubscriptions() {
        return mSubscriptions.hasSubscriptions();
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
                return Observable.merge(observable, Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));
                    }
                }));
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
