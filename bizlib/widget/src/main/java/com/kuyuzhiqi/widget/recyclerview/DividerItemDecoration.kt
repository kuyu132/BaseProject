package com.kuyuzhiqi.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * recycleView通用分割线
 */
class DividerItemDecoration(context: Context, orientation: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private var mDivider: Drawable
    private var mOrientation: Int = 0
    private var mShowLast = false

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }

    fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    override fun onDraw(canvas: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(canvas, parent)
        } else {
            drawHorizontal(canvas, parent)
        }
    }

    fun setShowLast() {
        mShowLast = true
    }

    private fun drawVertical(canvas: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = if (mShowLast) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: androidx.recyclerview.widget.RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = if (mShowLast) {
            parent.childCount
        } else {
            parent.childCount - 1
        }
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as androidx.recyclerview.widget.RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: androidx.recyclerview.widget.RecyclerView) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.intrinsicHeight)
        } else {
            outRect.set(0, 0, mDivider.intrinsicWidth, 0)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(android.R.attr.listDivider)

        val HORIZONTAL_LIST = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
    }
}