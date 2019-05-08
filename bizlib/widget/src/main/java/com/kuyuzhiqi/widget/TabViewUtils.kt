package com.kuyuzhiqi.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat


/**
 * 生成TabView的帮助类
 */
object TabViewUtils {

    /**
     * 主页tab（图标+文字）
     */
    @JvmOverloads
    fun getTabItemView(context: Context, tabIcon: Int?, tabTitle: String, @ColorRes tabSelectedColor: Int = R.color.selectable_text_selected): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_main_tab_view, null)

        if (null != tabIcon) {
            val imageView = view.findViewById<ImageView>(R.id.iv_tab_icon)
            imageView.setImageDrawable(generateIconDrawable(context, tabIcon, tabSelectedColor))
            imageView.visibility = View.VISIBLE
        }

        val textView = view.findViewById<TextView>(R.id.tv_tab_title)
        textView.setTextColor(generateTextColors(context, tabSelectedColor))
        textView.text = tabTitle

        return view
    }

    /**
     * 普通tab（主题色）
     */
    fun getTabItemView(context: Context, tabTitle: String): View {
        val tabView = LayoutInflater.from(context).inflate(R.layout.layout_common_tab_view, null)
        val tabTextView = tabView.findViewById<View>(R.id.tv_tab_title) as TextView
        tabTextView.text = tabTitle
        return tabView
    }

    /**
     * 图标自动着色
     */
    private fun generateIconDrawable(context: Context, @DrawableRes iconId: Int, @ColorRes tabSelectedColor: Int): Drawable {
        val colors = intArrayOf(ContextCompat.getColor(context, tabSelectedColor), ContextCompat.getColor(context, R.color.selectable_text_normal))
        val states = arrayOfNulls<IntArray>(2).apply {
            this[0] = intArrayOf(android.R.attr.state_selected)
            this[1] = intArrayOf()
        }
        val colorStateList = ColorStateList(states, colors)
        var drawable = ContextCompat.getDrawable(context, iconId)
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(states[0], drawable)//注意顺序
        stateListDrawable.addState(states[1], drawable)
        val state = stateListDrawable.constantState
        drawable = DrawableCompat.wrap(if (state == null) stateListDrawable else state.newDrawable()).mutate()
        DrawableCompat.setTintList(drawable, colorStateList)
        return drawable
    }

    /**
     * 文字自动着色
     */
    private fun generateTextColors(context: Context, @ColorRes tabSelectedColor: Int): ColorStateList {
        val colors = intArrayOf(ContextCompat.getColor(context, tabSelectedColor), ContextCompat.getColor(context, R.color.selectable_text_normal))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }
}
