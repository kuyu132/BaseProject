package com.kuyuzhiqi.baseproject

import android.os.Bundle
import com.kuyuzhiqi.bizbase.activity.BaseActivity
import com.kuyuzhiqi.utils.common.AntiPackageCaptureUtils

class MainActivity : BaseActivity() {
    override fun isActivityPageContainFragment(): Boolean {
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AntiPackageCaptureUtils.isWifiProxy(this)
    }
}
