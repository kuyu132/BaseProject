package com.kuyuzhiqi.utils.common;

import java.util.UUID;

public class StringUtils {

    /**
     * 生成唯一的UUID
     */
    public static String createUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 转化为Double
     *
     * @return 无法转换时返回null
     */
    public static Double convertToDouble(String str) {
        Double result = null;
        try {
            result = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断字符串是否为空
     */
    @Deprecated //TextUtils.isEmpty()
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
