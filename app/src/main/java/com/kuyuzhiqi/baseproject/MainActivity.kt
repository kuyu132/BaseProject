package com.kuyuzhiqi.baseproject

import android.os.Bundle
import com.kuyuzhiqi.bizbase.activity.BaseActivity

class MainActivity : BaseActivity() {
    override fun isActivityPageContainFragment(): Boolean {
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
