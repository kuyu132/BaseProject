package com.kuyuzhiqi.widget.listener;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kuyuzhiqi on 26/10/2017.
 * RecyclerView没有实现addOnItemTouchListener，只有接口
 * 监听整个item的点击事件，无法监听子item的监听事件(被拦截)
 */

public class MyRecycleViewItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;

    private OnClickListener mOnClicklistener;
    private OnLongClickListener mOnLongClickListener;
    private OnDoubleClickListener mOnDoubleClickListener;

    public MyRecycleViewItemClickListener(OnClickListener mOnClicklistener) {
        this.mOnClicklistener = mOnClicklistener;
    }

    public MyRecycleViewItemClickListener(OnLongClickListener mOnLongClickListener) {
        this.mOnLongClickListener = mOnLongClickListener;
    }

    public MyRecycleViewItemClickListener(OnDoubleClickListener mOnDoubleClickListener) {
        this.mOnDoubleClickListener = mOnDoubleClickListener;
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (mGestureDetector == null) {
            initGestureDetector(rv);
        }
        if (mGestureDetector.onTouchEvent(e)) { // 把事件交给GestureDetector处理
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化GestureDetector
     */
    private void initGestureDetector(final RecyclerView recyclerView) {
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() { // 这里选择SimpleOnGestureListener实现类，可以根据需要选择重写的方法

            /**
             * 单击事件
             */
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View view = recyclerView.findFocus();
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mOnClicklistener != null) {
                    mOnClicklistener.onClick(childView, recyclerView.getChildLayoutPosition(childView));
                    return true;
                }
                return false;
            }

            /**
             * 长按事件
             */
            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && mOnLongClickListener != null) {
                    mOnLongClickListener.onLongClick(childView, recyclerView.getChildLayoutPosition(childView));
                }
            }

            /**
             * 双击事件
             */
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && mOnDoubleClickListener != null) {
                        mOnDoubleClickListener.onDoubleClick(childView, recyclerView.getChildLayoutPosition(childView));
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * RecyclerView的Item点击事件监听接口和实现
     * 当ItemView的单击事件触发时调用
     */
    public interface OnClickListener {
        void onClick(View view, int position);
    }

    /**
     * 当ItemView的长按事件触发时调用
     */
    public interface OnLongClickListener {
        void onLongClick(View view, int position);
    }

    /**
     * 当ItemView的双击事件触发时调用
     */
    public interface OnDoubleClickListener {
        void onDoubleClick(View view, int position);
    }
}
