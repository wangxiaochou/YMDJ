package com.ypshengxian.daojia.utils;

import android.content.Context;

import com.ypshengxian.daojia.base.BaseApplication;


/**
 * <p> Utils初始化相关 </p><br>
 *
 * @author lwc
 * @date 2017/3/10 16:00
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class Utils {
    /** 保存Application的Context,供所有工具使用 */
    private static Context context;

    /**
     * 构造类
     */
    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        } else if (BaseApplication.getInstance() != null) {
            context = BaseApplication.getInstance();
            return context;
        }
        throw new NullPointerException("u should init first");
    }


}