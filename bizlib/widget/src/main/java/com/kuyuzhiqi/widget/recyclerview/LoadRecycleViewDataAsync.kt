package cn.smartinspection.widget.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 异步更新RecycleView中的TextView
 */
object LoadRecycleViewDataAsync {
    private val MAX_QUEUE_TASK_NUM = 30

    private var pool: ThreadPoolExecutor? = null
    private val disposables = CompositeDisposable()

    private fun initPool() {
        val parallelism = Runtime.getRuntime().availableProcessors()
        pool = ThreadPoolExecutor(parallelism, parallelism,
                0L, TimeUnit.MILLISECONDS,
                ArrayBlockingQueue<Runnable>(MAX_QUEUE_TASK_NUM),
                ThreadPoolExecutor.DiscardOldestPolicy())
    }

    public fun <T> addTask(task: LoadTask<T>, bundle: Bundle) {
        if (pool == null || pool!!.isShutdown) {
            initPool()
        }

        val disposable = Single.create(SingleOnSubscribe<LoadTask<T>> { emitter ->
            val result = task.compute(bundle)
            task.result = result
            emitter.onSuccess(task)
        }).subscribeOn(Schedulers.from(pool!!))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ task ->
                    val viewHolder = task.recyclerView.findViewHolderForAdapterPosition(task.position)
                    if (viewHolder != null) {
                        val textView = viewHolder.itemView.findViewById(task.textViewId) as? TextView?
                        if (textView != null && task.result != null) {
                            task.refreshTextView(textView, task.result!!, bundle)
                        }
                    }
                })
        disposables.add(disposable)
    }

    /**
     * onStop调用
     */
    public fun clearAll() {
        disposables.clear()
        pool?.shutdownNow()
    }
}

abstract class LoadTask<T>(val recyclerView: androidx.recyclerview.widget.RecyclerView,
                           val textViewId: Int, val position: Int) {
    var result: T? = null

    abstract fun compute(bundle: Bundle): T

    open fun refreshTextView(textView: TextView, result: T, bundle: Bundle) {
        textView.text = result.toString()
    }
}
