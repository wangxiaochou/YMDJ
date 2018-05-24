package com.ypshengxian.daojia.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 基于MVP的基础Presenter
 *
 * @author Yan
 * @date 2017.02.06
 * @note 1. 泛型I代表视图的接口，例如Activity，Fragment提供给Presenter的接口
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BasePresenter<I extends BaseView> {
    /** 持有View的引用 */
    protected Reference<I> mViewRef;

    /**
     * 关联视图
     *
     * @param view 视图的引用
     */
    public void attachView(I view) {
        mViewRef = new WeakReference<I>(view);
    }

    /**
     * 获得视图
     *
     * @return 视图的引用
     */
    public I getView() {

        return mViewRef != null ? mViewRef.get() : null;
    }

    /**
     * View是否已关联到此Presenter
     *
     * @return true -- 是  false -- 否
     */
    public boolean isViewAttached() {

        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 取消关联视图
     */
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}