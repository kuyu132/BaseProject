package com.kuyuzhiqi.widget.crumbview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuyuzhiqi.widget.R
import java.util.*

/**
 * Created by kuyuzhiqi on 20/12/2017.
 */

abstract class BaseBreadCrumbFragment<T> : androidx.fragment.app.Fragment {
    var mFragmentManager: androidx.fragment.app.FragmentManager? = null
    protected var mLevel = 1
    protected val mTopNode: T?
    protected var mNodeList: List<T> = ArrayList()
    protected var rv_list: androidx.recyclerview.widget.RecyclerView? = null

    constructor(level: Int, t: T) {
        this.mLevel = level
        this.mTopNode = t
    }

    constructor(level: Int, list: List<T>) {
        this.mLevel = level
        this.mTopNode = null
        this.mNodeList = list
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_base_bread_crumb_fragment, null)
        mFragmentManager = activity?.supportFragmentManager
        initViews(view)
        initData()
        return view
    }

    private fun initViews(view: View) {
        rv_list = view.findViewById(R.id.rv_list) as androidx.recyclerview.widget.RecyclerView
        rv_list!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
    }

    abstract fun initData()

    abstract fun getBreadCrumbTitle(t: T): String
    abstract fun getContainerId(): Int
}