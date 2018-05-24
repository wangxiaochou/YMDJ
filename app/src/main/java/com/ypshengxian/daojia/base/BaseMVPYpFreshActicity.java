package com.ypshengxian.daojia.base;

import android.os.Bundle;

/**
 * 基础
 *
 * @author Yan
 * @date 2018-02-07
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseMVPYpFreshActicity<I extends BaseView,P extends BasePresenter<I>> extends BaseYpFreshActivity {
    /** Presenter对象 */
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((I) this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    /**
     * 创建Presenter对象
     *
     * @return Presenter对象
     * @note 若需要使用MVP模式，则子类需实现此方法
     */
    protected abstract P createPresenter();

}
