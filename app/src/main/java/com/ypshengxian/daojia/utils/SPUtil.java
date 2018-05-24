package com.ypshengxian.daojia.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具
 *
 * @author Yan
 * @date 2017.02.16
 * @note 使用方法：
 * 1.使用全局配置：
 * // 仅在Application中初始化一次即可
 * SPUtil.init(getApplicationContext, "GlobalPrefName");
 * <p>
 * String useString = SPUtil.getGlobalString(key, defValue);
 * SPUtil.putGlobalString(key, value);
 * <p>
 * 2.使用自定义配置:
 * SPUtil spd = SPUtil.getPref(Context, "MyPref");
 * String useString = spd.getString(key, defValue);
 * spd.putString(key, value);
 * -------------------------------------------------------------------------------------------------
 * @modified mos
 * @date 2017.04.07
 * @note 改名SharedPrefs为SPUtil
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class SPUtil {
    /** 上下文 */
    private static Context sGlobalContext;
    /** 全局Shared Preference文件名 */
    private static String sGlobalSPName;
    /** 局部文件名 */
    private String mSharedPrefName;
    /** 局部上下文 */
    private Context mContext;

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param sharedPrefName SP名
     */
    public SPUtil(Context context, String sharedPrefName) {
        mContext = context;
        mSharedPrefName = sharedPrefName;
    }

    /**
     * 初始化
     *
     * @param appContext 应用上下文
     * @param globalSPName 全局SP名
     */
    public static void init(Context appContext, String globalSPName) {
        if (sGlobalContext == null && sGlobalSPName == null) {
            sGlobalContext = appContext;
            sGlobalSPName = globalSPName;
        }
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public static int getGlocalInt(String key, int defValue) {

        return getSP(sGlobalContext, sGlobalSPName).getInt(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public static boolean putGlobalInt(String key, int value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(sGlobalContext, sGlobalSPName);
        editor.putInt(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public static float getGlobalFloat(String key, float defValue) {

        return getSP(sGlobalContext, sGlobalSPName).getFloat(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public static boolean putGlobalFloat(String key, float value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(sGlobalContext, sGlobalSPName);
        editor.putFloat(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public static long getGlobalLong(String key, long defValue) {

        return getSP(sGlobalContext, sGlobalSPName).getLong(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public static boolean putGlobalLong(String key, long value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(sGlobalContext, sGlobalSPName);
        editor.putLong(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public static boolean getGlobalBoolean(String key, boolean defValue) {

        return getSP(sGlobalContext, sGlobalSPName).getBoolean(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public static boolean putGlobalBoolean(String key, boolean value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(sGlobalContext, sGlobalSPName);
        editor.putBoolean(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public static String getGlobalString(String key, String defValue) {

        return getSP(sGlobalContext, sGlobalSPName).getString(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public static boolean putGlobalString(String key, String value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(sGlobalContext, sGlobalSPName);
        editor.putString(key, value);

        return editor.commit();
    }

    /**
     * 是否包含key
     *
     * @param key 键
     * @return true -- 包含  false -- 不包含
     */
    public static boolean hasGlobalKey(String key) {

        return getSP(sGlobalContext, sGlobalSPName).contains(key);
    }

    /**
     * 获取Shared Preference
     *
     * @param context 上下文
     * @param sharedPrefName SP名
     * @return SP对象
     */
    private static SharedPreferences getSP(Context context, String sharedPrefName) {
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }

    /**
     * 获取Editor对象
     *
     * @param context 上下文
     * @param sharedPrefName SP名
     * @return Editor对象
     */
    private static Editor getEditor(Context context, String sharedPrefName) {

        return getSP(context, sharedPrefName).edit();
    }

    /**
     * 获取SharedPrefData对象
     *
     * @param context 上下文
     * @param sharedPrefName SP名
     * @return SharedPrefData对象
     */
    public static SPUtil getPref(Context context, String sharedPrefName) {

        return new SPUtil(context, sharedPrefName);
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public int getInt(String key, int defValue) {

        return getSP(mContext, mSharedPrefName).getInt(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public boolean putInt(String key, int value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(mContext, mSharedPrefName);
        editor.putInt(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public float getFloat(String key, float defValue) {

        return getSP(mContext, mSharedPrefName).getFloat(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public boolean putFloat(String key, float value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(mContext, mSharedPrefName);
        editor.putFloat(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public long getLong(String key, long defValue) {

        return getSP(mContext, mSharedPrefName).getLong(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public boolean putLong(String key, long value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(mContext, mSharedPrefName);
        editor.putLong(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public boolean getBoolean(String key, boolean defValue) {

        return getSP(mContext, mSharedPrefName).getBoolean(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public boolean putBoolean(String key, boolean value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(mContext, mSharedPrefName);
        editor.putBoolean(key, value);

        return editor.commit();
    }

    /**
     * 获取值
     *
     * @param key 键
     * @param defValue 默认值
     * @return 值
     */
    public String getString(String key, String defValue) {

        return getSP(mContext, mSharedPrefName).getString(key, defValue);
    }

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     * @return true -- 成功  false -- 失败
     */
    public boolean putString(String key, String value) {
        if (key == null || key.length() <= 0) {

            return false;
        }

        Editor editor = getEditor(mContext, mSharedPrefName);
        editor.putString(key, value);

        return editor.commit();
    }

    /**
     * 是否包含key
     *
     * @param key 键
     * @return true -- 包含  false -- 不包含
     */
    public boolean hasKey(String key) {

        return getSP(mContext, mSharedPrefName).contains(key);
    }
}
