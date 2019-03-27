package com.kuyuzhiqi.utils.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获取Application相关信息
 */
public class AppUtils {

    //获取包名
    public static String getPackageName(Context context) {
        return context.getPackageName().toString();
    }

    /**
     * 读取application节点的 meta-data 信息
     *
     * @return 字符串
     */
    public static String readStringFromApplicationMetaData(Context context, String metaDataKey) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(metaDataKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getVersionName(Context context) {
        try {
            String packageName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取versioncode
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 读取application节点的 meta-data 信息
     *
     * @return 布尔值
     */

    public static boolean readBooleanFromApplicationMetaData(Context context, String metaDataKey) {
        boolean value = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getBoolean(metaDataKey, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
