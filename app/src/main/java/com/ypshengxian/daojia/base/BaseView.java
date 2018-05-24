package com.ypshengxian.daojia.base;

import android.content.Context;

import com.ypshengxian.daojia.view.LoadingDialog;


/**
 * 基础视图类
 *
 * @author Yan
 * @date 2017.02.07
 * @note 1. 定义Activity，Fragment需要实现的通用方法
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface BaseView {
    /**
     * 获取标志
     *
     * @return 标志字符串
     * @note 标志可用于打印调试信息
     */
    String TAG();

    /**
     * 显示Toast
     *
     * @param msg 消息
     */
    void showToast(String msg);

    /**
     * 获得框架基类Activity
     *
     * @return BaseActivity
     */
    BaseActivity getBaseActivity();

    /**
     * 获得上下文
     *
     * @return Activity获得this，Fragment获得onAttach的Context
     */
    Context getContext();

    /**
     * 显示Loading
     */
    void showLoading();

    /**
     * 显示Loading
     *
     * @param option 参数
     */
    void showLoading(LoadingDialog.Option option);

    /**
     * 隐藏Loading
     */
    void hideLoading();
}
