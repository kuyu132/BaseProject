package cn.smartinspection.widget.crumbview

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kuyuzhiqi.widget.R

/**
 * Created by kuyuzhiqi on 20/12/2017.
 */

class BreadCrumbView(context: Context, attrs: AttributeSet? = null) : HorizontalScrollView(context, attrs) {

    private val mContainer: LinearLayout = LinearLayout(context)
    private var LIGHT_COLOR: Int = 0
    private var DARK_COLOR: Int = 0
    var stakeChangeListener: OnStakeChangeListener? = null

    init {
        initView()
    }

    fun initView() {
        mContainer.orientation = LinearLayout.HORIZONTAL
        val res: Resources = context.resources
        mContainer.setPadding(res.getDimensionPixelOffset(R.dimen.common_left_right), 0,
                res.getDimensionPixelOffset(R.dimen.common_left_right), 0)
        mContainer.gravity = Gravity.CENTER_VERTICAL

        LIGHT_COLOR = res.getColor(R.color.theme_primary)
        DARK_COLOR = res.getColor(R.color.second_text_color)
        addView(mContainer)
    }

    fun addChild(name: String) {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_bread_crumb_view, null)
        val tv: TextView = itemView.findViewById(R.id.crumb_name) as TextView
        tv.setText(name)
        tv.setOnClickListener {
            val numCrumbs: Int = mContainer.childCount
            val index = mContainer.indexOfChild(itemView)
            if (index < numCrumbs - 1) {
                val offset = numCrumbs - index - 1
                stakeChangeListener?.popBack(index, offset)
                for (i in 0 until offset) {
                    mContainer.removeViewAt(mContainer.childCount - 1)
                }
                highLightIndex()
            }
        }
        mContainer.addView(itemView)
        highLightIndex()
    }

    fun highLightIndex() {
        // 面包屑的数量
        val numCrumbs = mContainer.childCount
        for (i in 0 until numCrumbs) {
            val view: View = mContainer.getChildAt(i)
            val text: TextView = view.findViewById(R.id.crumb_name) as TextView
            val image: ImageView = view.findViewById(R.id.crumb_icon) as ImageView
            if (i == numCrumbs - 1) {
                text.setTextColor(DARK_COLOR)
                image.visibility = View.GONE
            } else {
                text.setTextColor(LIGHT_COLOR)
                image.visibility = View.VISIBLE
            }
        }
        stakeChangeListener?.onCountChange()
    }

    fun popBack() {
        if (mContainer.childCount > 0) {
            mContainer.removeViewAt(mContainer.childCount - 1)
            highLightIndex()
        }
    }

    fun getItemCount(): Int {
        return mContainer.childCount
    }

    interface OnStakeChangeListener {
        fun popBack(index: Int, offset: Int)
        fun onCountChange()
    }
}