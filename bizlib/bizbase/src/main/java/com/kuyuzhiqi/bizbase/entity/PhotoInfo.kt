package com.kuyuzhiqi.bizbase.entity

import java.io.Serializable

/**
 * 显示图片的实体
 */
class PhotoInfo : Serializable {
    var md5: String? = null
    var url: String? = null
    var path: String? = null
    var showType: Int = 0
    var longitude: Double? = null
    var latitude: Double? = null
    var address: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val photoInfo = o as PhotoInfo?

        return if (md5 != null) md5 == photoInfo!!.md5 else photoInfo!!.md5 == null
    }

    override fun hashCode(): Int {
        return if (md5 != null) md5!!.hashCode() else 0
    }
}