package com.kuyuzhiqi.bizbase.utils;

import com.google.gson.*;

import java.util.Map;

/**
 * Gson工具类
 */
public class GsonUtils {

    private static Gson sGson = null;
    private static JsonParser sJsonParser = null;

    static {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .setLenient()//返回的字符串中可能包含特定的字符,如: NUL \0 ,会导致解释出错. 所以尝试使用 Lenient mode
                    .create();
        }
        if (sJsonParser == null) {
            sJsonParser = new JsonParser();
        }
    }

    private GsonUtils() {

    }

    public static Gson getGson() {
        return sGson;
    }

    public static String toJson(Object o) {
        return sGson.toJson(o);
    }

    public static JsonObject convertStringToJsonObject(String jsString) {
        return sJsonParser.parse(jsString).getAsJsonObject();
    }

    public static JsonArray convertStringToJsonArrsay(String jsString) {
        return sJsonParser.parse(jsString).getAsJsonArray();
    }

    public static <T> String mapToJson(Map<String, T> map) {
        String result = sGson.toJson(map);
        return result;
    }
}
