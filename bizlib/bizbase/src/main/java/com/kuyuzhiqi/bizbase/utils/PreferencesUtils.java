package com.kuyuzhiqi.bizbase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.kuyuzhiqi.utils.common.EncryptUtils;

/**
 * SharedPreferences操作
 */
public class PreferencesUtils {
    private static final String DEFAULT_SP_NAME = "default_sp";
    /**
     * 根据用户id设定的前缀
     */
    private static final String USER_SP_NAME_PREFIX = "u_";

    private Context context;
    private static PreferencesUtils instance = null;
    private static SharedPreferences defaultSp = null;
    private static Long sUserId;

    private PreferencesUtils(Context context) {
        this.context = context.getApplicationContext();
        defaultSp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesUtils getInstance() {
        if (instance == null) {
            throw new IllegalStateException(PreferencesUtils.class.getSimpleName()
                    + " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    /**
     * 在Application初始化默认sp
     */
    public static void initializeInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtils(context.getApplicationContext());
        }
    }

    /**
     * 切换用户，记录当前用户id
     */
    public void switchUser(Long userId) {
        sUserId = userId;
    }

    /**
     * 获取当前用户的sp实例
     */
    private SharedPreferences getUserSp() {
        return context.getSharedPreferences(USER_SP_NAME_PREFIX + sUserId, Context.MODE_PRIVATE);
    }

    public boolean putString(String key, String value) {
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return defaultSp.getString(key, defaultValue);
    }

    public boolean putStringEncrypted(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putString(key, EncryptUtils.simpleEncrypt(value));
        return editor.commit();
    }

    public String getStringEncrypted(String key, String defaultValue) {
        return EncryptUtils.simpleDecrypt(getString(key, defaultValue));
    }

    public boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return defaultSp.getInt(key, defaultValue);
    }

    public boolean putLong(String key, long value) {
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getLong(String key) {
        return getLong(key, -1);
    }

    public long getLong(String key, long defaultValue) {
        return defaultSp.getLong(key, defaultValue);
    }

    public boolean putFloat(String key, float value) {
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getFloat(String key) {
        return getFloat(key, -1);
    }

    public float getFloat(String key, float defaultValue) {
        return defaultSp.getFloat(key, defaultValue);
    }

    public boolean putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = defaultSp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return defaultSp.getBoolean(key, defaultValue);
    }

    public void deleteKey(String key) {
        defaultSp.edit().remove(key);
    }

    public boolean putUserString(String key, String value) {
        SharedPreferences.Editor editor = getUserSp().edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getUserString(String key) {
        return getUserString(key, null);
    }

    public String getUserString(String key, String defaultValue) {
        return getUserSp().getString(key, defaultValue);
    }

    public boolean putUserInt(String key, int value) {
        SharedPreferences.Editor editor = getUserSp().edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getUserInt(String key) {
        return getUserSp().getInt(key, -1);
    }

    public int getUserInt(String key, int defaultValue) {
        return getUserSp().getInt(key, defaultValue);
    }

    public boolean putUserLong(String key, long value) {
        SharedPreferences.Editor editor = getUserSp().edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public long getUserLong(String key) {
        return getUserSp().getLong(key, -1);
    }

    public long getUserLong(String key, long defaultValue) {
        return getUserSp().getLong(key, defaultValue);
    }

    public boolean putUserFloat(String key, float value) {
        SharedPreferences.Editor editor = getUserSp().edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public float getUserFloat(String key) {
        return getUserSp().getFloat(key, -1);
    }

    public float getUserFloat(String key, float defaultValue) {
        return getUserSp().getFloat(key, defaultValue);
    }

    public boolean putUserBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getUserSp().edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean getUserBoolean(String key) {
        return getUserSp().getBoolean(key, false);
    }

    public boolean getUserBoolean(String key, boolean defaultValue) {
        return getUserSp().getBoolean(key, defaultValue);
    }

    public void deleteUserKey(String key) {
        getUserSp().edit().remove(key).commit();
    }

    public boolean isContainUserKey(String key) {
        return getUserSp().contains(key);
    }

    public void clearUserAllSp() {
        getUserSp().edit().clear().commit();
    }
}
