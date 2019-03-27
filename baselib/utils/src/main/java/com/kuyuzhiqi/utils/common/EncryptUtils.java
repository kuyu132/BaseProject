package com.kuyuzhiqi.utils.common;

import android.util.Base64;

/**
 * 加密工具
 */
public class EncryptUtils {

    private EncryptUtils(){}

    /**
     * 简单加密,不安全
     * @param input
     * @return
     */
    public static String simpleEncrypt(String input){
        return Base64.encodeToString(input.getBytes(), Base64.URL_SAFE);
    }

    public static String simpleDecrypt(String input){
        return new String(Base64.decode(input, Base64.URL_SAFE));
    }

}
