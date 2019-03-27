package cn.smartinspection.widget.crumbview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.smartinspection.widget.R
import java.util.ArrayList

/**
 * Created by kuyuzhiqi on 20/12/2017.
 */

abstract class BaseBreadCrumbFragment<T> : Fragment {
    var mFragmentManager: FragmentManager? = null
    protected var mLevel = 1
    protected val mTopNode: T?
    protected var mNodeList: List<T> = ArrayList()
    protected var rv_list: RecyclerView? = null

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
        rv_list = view.findViewById(R.id.rv_list) as RecyclerView
        rv_list!!.layoutManager = LinearLayoutManager(context)
    }

    abstract fun initData()

    abstract fun getBreadCrumbTitle(t: T): String
    abstract fun getContainerId(): Int
}