package com.ypshengxian.daojia.network.base;


import com.ypshengxian.daojia.utils.ExceptionUtil;
import com.ypshengxian.daojia.utils.LogUtils;

import rx.functions.Action1;

/**
 * 模块调用错误的动作调用错误的动作
 *
 * @author Yan
 * @date 2017.07.21
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ModuleBaseErrorAction implements Action1<Throwable> {

    @Override
    public void call(Throwable throwable) {
        processThrowable(throwable);
        onError(throwable);
    }

    /**
     * 回调
     * @param throwable 错误
     */
    public abstract void onError(Throwable throwable);

    /**
     * 处理异常
     *
     * @param throwable 异常
     */
    private void processThrowable(Throwable throwable) {
        // 这里可以添加通用的处理
        // 打印错误
        if (throwable instanceof Exception) {
            LogUtils.e("异常情况", ExceptionUtil.getStackTrace((Exception) throwable));
        } else {
            LogUtils.e("异常情况", throwable);
        }
    }
}
