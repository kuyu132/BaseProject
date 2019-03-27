package com.kuyuzhiqi.bizbase.utils

import com.kuyuzhiqi.bizbase.BizConstant
import java.util.*

object LanguageUtils {

    /**
     * 因为locale.getLanguage()得到的香港和中国都是zh，无法区别所以用country
     * @return country的小写
     */
    val country: String
        get() {
            val locale = Locale.getDefault()
            return locale.country
        }

    val languageFolderName: String
        get() {
            val country = country
            return if (country == BizConstant.LANGUAGE_TYPE.CN) {
                country.toLowerCase()
            } else if (country == BizConstant.LANGUAGE_TYPE.HK || country == BizConstant.LANGUAGE_TYPE.TW) {
                BizConstant.LANGUAGE_TYPE.HK.toLowerCase()
            } else if (country == BizConstant.LANGUAGE_TYPE.JP) {
                country.toLowerCase()
            } else if (country == BizConstant.LANGUAGE_TYPE.US) {
                country.toLowerCase()
            } else {
                BizConstant.LANGUAGE_TYPE.CN.toLowerCase()
            }
        }
}
