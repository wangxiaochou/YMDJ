package com.ypshengxian.daojia.network.base;


import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.utils.ResUtils;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


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
public abstract class ModuleErrorAction<T extends BaseView> extends ModuleBaseErrorAction {
    /** 基础Presenter */
    private BasePresenter<T> mPresenter;

    /**
     * 构造函数
     *
     * @param presenter 基础presenter
     */
    public ModuleErrorAction(BasePresenter<T> presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onError(Throwable throwable) {
        T view = mPresenter.getView();
        if (view != null) {
            processThrowable(view, throwable);
            onError(view, throwable, null);
            if (throwable instanceof SocketTimeoutException ||
                    throwable instanceof SocketException ||
                    throwable instanceof UnknownHostException) {

                view.showToast(ResUtils.getString(R.string.toast_network_timeout));

                onNetworkError(view, throwable, null);
            } else {
                onClientError(view, throwable, null);
            }
        }
    }

    /**
     * 处理异常
     *
     * @param view 视图
     * @param throwable 异常
     */
    private void processThrowable(final BaseView view, Throwable throwable) {
        // 通用的处理
        view.hideLoading();
        if (view instanceof BaseYpFreshActivity) {
            ((BaseYpFreshActivity) view).hideLoading();
        } else if (view instanceof BaseYpFreshFragment) {
            ((BaseYpFreshFragment) view).hideLoading();
        }
    }

    /**
     * 错误
     *
     * @param view 视图
     * @param throwable 异常
     * @param onRetryClickListener 重试点击监听
     */
    public void onError(T view, Throwable throwable, final OnRetryClickListener onRetryClickListener) {

    }

    /**
     * 客户端异常
     *
     * @param view 视图
     * @param throwable 异常
     * @param onRetryClickListener 重试点击监听
     */
    public void onClientError(T view, Throwable throwable, final OnRetryClickListener onRetryClickListener) {

    }

    /**
     * 网络异常
     *
     * @param view 视图
     * @param throwable 异常
     * @param onRetryClickListener 重试点击监听
     */
    public void onNetworkError(T view, Throwable throwable, final OnRetryClickListener onRetryClickListener) {

    }

    /**
     * 重试点击监听接口
     */
    public interface OnRetryClickListener {
        /**
         * 重试被点击
         */
        void onRetryClick();
    }
}
