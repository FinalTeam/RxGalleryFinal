package cn.finalteam.rxgalleryfinal.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 事件总线
 * @see {@link 'http://blog.metova.com/how-to-use-rxjava-as-an-event-bus/'}
 */
public class RxBus {
    private RxBus() {
    }

    private static final RxBus INSTANCE = new RxBus();
    public static RxBus getDefault() {
        return INSTANCE;
    }

    private final Subject<Object, Object> mBusSubject
            = new SerializedSubject<>(PublishSubject.create());

    public <T> Observable<T> register(Class<T> eventClass) {
        if (eventClass == null) {
            throw new NullPointerException("'eventClass' can not be empty.");
        }
        return mBusSubject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj);
    }

    public void post(Object event) {
        if (event == null) {
            throw new NullPointerException("'event' can not be empty.");
        }
        mBusSubject.onNext(event);
    }

}
