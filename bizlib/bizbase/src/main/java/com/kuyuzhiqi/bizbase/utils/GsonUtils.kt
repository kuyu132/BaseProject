package com.kuyuzhiqi.bizbase.utils

import com.google.gson.*

/**
 * Gson工具类
 */
object GsonUtils {

    var gson: Gson? = null
        private set
    private var sJsonParser: JsonParser? = null

    init {
        if (gson == null) {
            gson = GsonBuilder()
                .setLenient()//返回的字符串中可能包含特定的字符,如: NUL \0 ,会导致解释出错. 所以尝试使用 Lenient mode
                .create()
        }
        if (sJsonParser == null) {
            sJsonParser = JsonParser()
        }
    }

    fun toJson(o: Any): String {
        return gson!!.toJson(o)
    }

    fun convertStringToJsonObject(jsString: String): JsonObject {
        return sJsonParser!!.parse(jsString).asJsonObject
    }

    fun convertStringToJsonArrsay(jsString: String): JsonArray {
        return sJsonParser!!.parse(jsString).asJsonArray
    }

    fun <T> mapToJson(map: Map<String, T>): String {
        return gson!!.toJson(map)
    }
}
