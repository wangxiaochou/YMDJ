package com.ypshengxian.daojia.network.base;


import rx.functions.Action1;

/**
 * 模块调用错误的动作调用成功的动作
 *
 * @author Yan
 * @date 2017.07.20
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ModuleBaseSuccessAction<R extends BaseModuleApiResult> implements Action1<R> {
    @Override
    public void call(R data) {
        processData(data);
        onSuccess(data);
    }

    /**
     * 回调
     *
     * @param data 数据
     * @note 此回调的view，一定是没有被释放的，可不做判断，直接使用
     */
    public abstract void onSuccess(R data);

    /**
     * 处理数据
     *
     * @param data 返回数据
     * @return true -- 拦截通用处理 false -- 不拦截通用处理
     * @note 这里可以处理一些通用的错误代码
     */
    private void processData(BaseModuleApiResult data) {
        // 这里可以添加通用的处理
    }
}
