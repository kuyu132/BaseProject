package com.kuyuzhiqi.utils.common;

import java.text.DecimalFormat;

/**
 * 保留小数工具
 */
public class DecimalFormatUtils {

    /**
     * 保留一位小数
     */
    public static String getDecimalFormatJustOne(Object value){
        return new DecimalFormat("#0.0").format(value);
    }

    /**
     * 保留两位小数
     */
    public static String getDecimalFormat(Object value) {
        return new DecimalFormat("#0.00").format(value);
    }

    /**
     * 最多保留两位小数
     */
    public static String getDecimalFormatAtMost(Object value){
        return new DecimalFormat("#.##").format(value);
    }

}
