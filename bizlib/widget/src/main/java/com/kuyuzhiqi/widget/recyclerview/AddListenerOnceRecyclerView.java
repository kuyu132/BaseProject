package cn.smartinspection.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 只能增加一次ItemTouchListener
 */
public class AddListenerOnceRecyclerView extends RecyclerView {

    private boolean mIsAddItemTouchListenerBefore = false;

    public AddListenerOnceRecyclerView(Context context) {
        super(context);
    }

    public AddListenerOnceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AddListenerOnceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addOnItemTouchListener(OnItemTouchListener listener) {
        if (!mIsAddItemTouchListenerBefore) {
            super.addOnItemTouchListener(listener);
            mIsAddItemTouchListenerBefore = true;
        }
    }
}
