package com.kuyuzhiqi.bizbase.utils;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * RxJava实现简单的事件总线。
 * 使用完毕需要在生命周期结束的地方取消订阅事件。
 * http://www.tuicool.com/articles/e6RJfaV
 */
public class RxBus {
    private final Subject<Object> mBus;

    private RxBus(){
        mBus = PublishSubject.create().toSerialized();
    }

    // 单例RxBus
    public static RxBus getDefault() {
        return Holder.BUS;
    }

    // 发送一个新的事件
    public void post(Object obj) {
        mBus.onNext(obj);
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }
}
