package com.kuyuzhiqi.utils.common;

/**
 * 输入相关的工具类
 */
public class InputUtils {

    private static final long CLICK_TIME_INTERVAL = 500;
    private static long lastClickTime;

    private InputUtils() {
    }

    /**
     * 是否在短时间内点击了多次
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long diff = time - lastClickTime;
        if (0 < diff && diff < CLICK_TIME_INTERVAL) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
