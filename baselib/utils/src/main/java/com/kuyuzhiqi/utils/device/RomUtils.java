package com.kuyuzhiqi.utils.device;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * 判断当前设备的ROM类型
 */
public class RomUtils {

    private static final String KEY_HUAWEI_VERSION = "ro.confg.hw_systemversion";
    private static final String KEY_MIUI_VERSION = "ro.miui.ui.version.name";

    private static RomUtils sInstance = null;
    private static Object mLockObj = new Object();
    private Context mContext;

    private Boolean mIsMIUI = null;
    private Boolean mIsHuaWeiRom = null;

    private RomUtils(Context context){
        mContext = context.getApplicationContext();
    }

    public static RomUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (mLockObj) {
                if (sInstance == null) {
                    sInstance = new RomUtils(context);
                }
            }
        }
        return sInstance;
    }

    /**
     * 是否小米的ROM
     * @return
     */
    public boolean isXiaoMiRom() {
        if (mIsMIUI == null) {
            mIsMIUI = hasRomVersionValue(KEY_MIUI_VERSION);
        }
        return mIsMIUI;
    }

    /**
     * 是否华为的ROM
     * @return
     */
    public boolean isHuaWeiRom() {
        if (mIsHuaWeiRom == null) {
            mIsHuaWeiRom = hasRomVersionValue(KEY_HUAWEI_VERSION);
        }
        return mIsHuaWeiRom;
    }

    /**
     * 是否有某Rom的版本信息
     * @param romVersionKey
     * @return
     */
    private boolean hasRomVersionValue(String romVersionKey) {
        String value = getSystemProperties(romVersionKey);
        boolean b = value != null && !value.isEmpty();
        return b;
    }

    private String getSystemProperties(String key) {
        try {
            ClassLoader classLoader = mContext.getClassLoader();
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            Method methodGet = SystemProperties.getMethod("get", String.class);
            return (String) methodGet.invoke(SystemProperties, key);
        } catch (Exception e) {
            return null;
        }
    }

}
