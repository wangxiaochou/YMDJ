package com.ypshengxian.daojia.utils;

import android.app.Activity;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.view.SimpleTitleBarBuilder;


/**
 * 页面
 *
 * @author Yan
 * @date 2018-02-08
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class TitleUtils {
    /**
     * 为页面设置标题栏
     *
     * @param activity 必须当前页面Activity   this 代替
     * @param title 标题
     * @param bgColor 标题栏背景
     * @param tlColor 标题颜色
     * @param tlSize 标题字体大小
     */
    public static void setTitleBar(final Activity activity, @StringRes int title,
                                   @ColorRes int bgColor, @ColorRes int tlColor, float tlSize) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(bgColor))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(tlColor))
                .setTitleSize(tlSize)
                .setShadowHeight(0)
                .setTitleText(ResUtils.getString(title));
    }

    /**
     * 为页面设置标题栏
     *
     * @param activity 必须当前页面Activity   this 代替
     * @param title 标题
     */
    public static void setTitleBar(final Activity activity, @StringRes int title) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0)
                .setTitleText(ResUtils.getString(title));
    }
    /**
     * 为页面设置标题栏
     *
     * @param activity 必须当前页面Activity   this 代替
     * @param title 标题
     */
    public static void setTitleBar(final Activity activity, String title) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0)
                .setTitleText(title);
    }

    /**
     * 为页面设置标题栏
     *
     * @param activity 必须当前页面Activity   this 代替
     * @param title 标题
     */
    public static void setTitleBar(final Activity activity, String title,boolean leftVisble) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setLeftVisible(leftVisble)
                .setShadowHeight(0)
                .setTitleText(title);
    }

    /**
     * 为页面设置标题栏
     *
     * @param activity   必须传入当前activity   this 代替
     * @param centerTitle  中间的标题
     * @param rightTitle   右边的标题
     * @param listener    右边标题的监听
     */
    public static void setTitleBar(final Activity activity, @StringRes int centerTitle,
                                   @StringRes int rightTitle, View.OnClickListener listener) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0)
                .setTitleText(ResUtils.getString(centerTitle))
                .setRightText(ResUtils.getString(rightTitle))
                .setRightTextSize(16)
                .setRightTextColor(ResUtils.getColor(R.color.color_white))
                .setRightClickListener(listener);
    }

    /**
     * 为页面设置标题栏
     *
     * @param activity   必须传入当前activity
     * @param centerTitle  中间的标题
     * @param rightTitle   右边的标题
     * @param listener    右边标题的监听
     */
    public static void setTitleBar(final Activity activity, @StringRes int centerTitle,
                                   String rightTitle, View.OnClickListener listener) {
        SimpleTitleBarBuilder.attach(activity)
                .setBackgroundColor(ResUtils.getColor(R.color.color_theme))
                .setLeftDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_shop_ge_back))
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                })
                .setTitleColor(ResUtils.getColor(R.color.color_white))
                .setTitleSize(18)
                .setShadowHeight(0)
                .setTitleText(ResUtils.getString(centerTitle))
                .setRightText(rightTitle)
                .setRightTextSize(16)
                .setRightTextColor(ResUtils.getColor(R.color.color_white))
                .setRightClickListener(listener);
    }

}
