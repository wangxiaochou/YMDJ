package com.ypshengxian.daojia.base;

import android.os.Bundle;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-02-07
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseMVPYpFreshFragment<I extends BaseView,P extends BasePresenter<I>> extends BaseYpFreshFragment {
    /** Presenter对象 */
    public P mPresenter;

    /**
     * 创建Presenter对象
     *
     * @return Presenter对象
     * @note 若需要使用MVP模式，则子类需实现此方法
     */
    protected abstract P createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建Presenter
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((I) this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }
}
