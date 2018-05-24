package com.ypshengxian.daojia.network.base;

import rx.functions.Action0;

/**
 * 子模块调用错误的动作
 *
 * @author Yan
 * @date 2017.07.21
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ModuleBaseCompleteAction implements Action0 {
    @Override
    public void call() {
        onComplete();
    }

    /**
     * 回调
     */
    public abstract void onComplete();
}
