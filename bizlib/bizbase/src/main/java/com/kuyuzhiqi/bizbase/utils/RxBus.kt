package com.kuyuzhiqi.bizbase.utils


import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * RxJava实现简单的事件总线。
 * 使用完毕需要在生命周期结束的地方取消订阅事件。
 * http://www.tuicool.com/articles/e6RJfaV
 */
class RxBus private constructor() {
    private val mBus: Subject<Any>

    init {
        mBus = PublishSubject.create<Any>().toSerialized()
    }

    // 发送一个新的事件
    fun post(obj: Any) {
        mBus.onNext(obj)
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    fun <T> toObservable(tClass: Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    private object Holder {
        val BUS = RxBus()
    }

    companion object {

        // 单例RxBus
        val default: RxBus
            get() = Holder.BUS
    }
}
