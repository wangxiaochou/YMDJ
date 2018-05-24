package com.ypshengxian.daojia.network.base;


import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseView;

/**
 * 模块调用错误的动作调用成功的动作
 *
 * @author Yan
 * @date 2017.07.21
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ModuleCompleteAction<T extends BaseView> extends ModuleBaseCompleteAction {
    /** 基础Presenter */
    public BasePresenter<T> mPresenter;

    /**
     * 构造函数
     *
     * @param presenter 基础presenter
     */
    public ModuleCompleteAction(BasePresenter<T> presenter) {

        mPresenter = presenter;
    }

    @Override
    public void onComplete() {

        T view = mPresenter.getView();
        if (null != view) {
            onComplete(view);
        }
    }

    /**
     * 回调
     *
     * @param view 视图
     * @note 此回调的view，一定是没有被释放的，可不做判断，直接使用
     */
    public abstract void onComplete(T view);
}
