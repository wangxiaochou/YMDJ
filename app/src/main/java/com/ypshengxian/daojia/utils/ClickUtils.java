package com.ypshengxian.daojia.utils;

/**
 * <p>点击帮助类</p><br>
 *
 * @author - lwc
 * @date - 2017/11/3
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ClickUtils {
    /** 快速点击设置的间隔 */
    private final static long INTERVAL_FAST_CLICK = 200;
    /** 记录时间 */
    private static long sClickTimeMills = 0;

    /**
     * 判断是否是快速点击
     *
     * @return true - 快速点击 false - 正常逻辑
     */
    public static boolean isFastClick() {
        long nowTimeMills = TimeUtils.getNowTimeMills();
        if (nowTimeMills - sClickTimeMills < INTERVAL_FAST_CLICK) {
            return true;
        }
        sClickTimeMills = TimeUtils.getNowTimeMills();
        return false;
    }
}
