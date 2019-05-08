package com.kuyuzhiqi.bizbase.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.kuyuzhiqi.utils.common.EncryptUtils

/**
 * SharedPreferences操作
 */
class SharePreferencesUtils private constructor(context: Context) {

    private val context: Context

    /**
     * 获取当前用户的sp实例
     */
    private val userSp: SharedPreferences
        get() = context.getSharedPreferences(USER_SP_NAME_PREFIX + sUserId!!, Context.MODE_PRIVATE)

    init {
        this.context = context.applicationContext
        defaultSp = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 切换用户，记录当前用户id
     */
    fun switchUser(userId: Long?) {
        sUserId = userId
    }

    fun putString(key: String, value: String): Boolean {
        val editor = defaultSp!!.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getString(key: String, defaultValue: String? = null): String? {
        return defaultSp!!.getString(key, defaultValue)
    }

    fun putStringEncrypted(key: String, value: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            return false
        }
        val editor = defaultSp!!.edit()
        editor.putString(key, EncryptUtils.simpleEncrypt(value))
        return editor.commit()
    }

    fun getStringEncrypted(key: String, defaultValue: String): String {
        return EncryptUtils.simpleDecrypt(getString(key, defaultValue))
    }

    fun putInt(key: String, value: Int): Boolean {
        val editor = defaultSp!!.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getInt(key: String, defaultValue: Int = -1): Int {
        return defaultSp!!.getInt(key, defaultValue)
    }

    fun putLong(key: String, value: Long): Boolean {
        val editor = defaultSp!!.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getLong(key: String, defaultValue: Long = -1): Long {
        return defaultSp!!.getLong(key, defaultValue)
    }

    fun putFloat(key: String, value: Float): Boolean {
        val editor = defaultSp!!.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getFloat(key: String, defaultValue: Float = -1f): Float {
        return defaultSp!!.getFloat(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean): Boolean {
        val editor = defaultSp!!.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return defaultSp!!.getBoolean(key, defaultValue)
    }

    fun deleteKey(key: String) {
        defaultSp!!.edit().remove(key)
    }

    fun putUserString(key: String, value: String): Boolean {
        val editor = userSp.edit()
        editor.putString(key, value)
        return editor.commit()
    }

    @JvmOverloads
    fun getUserString(key: String, defaultValue: String? = null): String? {
        return userSp.getString(key, defaultValue)
    }

    fun putUserInt(key: String, value: Int): Boolean {
        val editor = userSp.edit()
        editor.putInt(key, value)
        return editor.commit()
    }

    fun getUserInt(key: String): Int {
        return userSp.getInt(key, -1)
    }

    fun getUserInt(key: String, defaultValue: Int): Int {
        return userSp.getInt(key, defaultValue)
    }

    fun putUserLong(key: String, value: Long): Boolean {
        val editor = userSp.edit()
        editor.putLong(key, value)
        return editor.commit()
    }

    fun getUserLong(key: String): Long {
        return userSp.getLong(key, -1)
    }

    fun getUserLong(key: String, defaultValue: Long): Long {
        return userSp.getLong(key, defaultValue)
    }

    fun putUserFloat(key: String, value: Float): Boolean {
        val editor = userSp.edit()
        editor.putFloat(key, value)
        return editor.commit()
    }

    fun getUserFloat(key: String): Float {
        return userSp.getFloat(key, -1f)
    }

    fun getUserFloat(key: String, defaultValue: Float): Float {
        return userSp.getFloat(key, defaultValue)
    }

    fun putUserBoolean(key: String, value: Boolean): Boolean {
        val editor = userSp.edit()
        editor.putBoolean(key, value)
        return editor.commit()
    }

    fun getUserBoolean(key: String): Boolean {
        return userSp.getBoolean(key, false)
    }

    fun getUserBoolean(key: String, defaultValue: Boolean): Boolean {
        return userSp.getBoolean(key, defaultValue)
    }

    fun deleteUserKey(key: String) {
        userSp.edit().remove(key).commit()
    }

    fun isContainUserKey(key: String): Boolean {
        return userSp.contains(key)
    }

    fun clearUserAllSp() {
        userSp.edit().clear().commit()
    }

    companion object {
        private val DEFAULT_SP_NAME = "default_sp"
        /**
         * 根据用户id设定的前缀
         */
        private val USER_SP_NAME_PREFIX = "u_"
        private var instance: SharePreferencesUtils? = null
        private var defaultSp: SharedPreferences? = null
        private var sUserId: Long? = null

        fun getInstance(): SharePreferencesUtils {
            if (instance == null) {
                throw IllegalStateException(SharePreferencesUtils::class.java.simpleName + " is not initialized, call initializeInstance(..) method first.")
            }
            return instance!!
        }

        /**
         * 在Application初始化默认sp
         */
        fun initializeInstance(context: Context) {
            if (instance == null) {
                instance = SharePreferencesUtils(context.applicationContext)
            }
        }
    }
}
