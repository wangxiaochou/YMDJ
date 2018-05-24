package com.ypshengxian.daojia.network.base;


import android.os.Bundle;

import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.preference.YPPreference;
import com.ypshengxian.daojia.ui.activity.LoginActivity;
import com.ypshengxian.daojia.utils.LogUtils;

/**
 * 模块调用错误的动作调用成功的动作
 *
 * @author Yan
 * @date 2017.07.21
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class ModuleSuccessAction<T extends BaseView, R extends BaseModuleApiResult> extends ModuleBaseSuccessAction<R> {
    /** 基础Presenter */
    private BasePresenter<T> mPresenter;

    /**
     * 构造函数
     *
     * @param presenter 基础presenter
     */
    public ModuleSuccessAction(BasePresenter<T> presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSuccess(R data) {
        T view = mPresenter.getView();
        if (view != null) {
            processData(view, data);
            LogUtils.i("网络请求 ", "end");
            if(null==data.error){
                onSuccess(view,data);
            }else {
                if(null!=data.error){
                    if(data.error.code.equals("not_login")){
                        Bundle bundle=new Bundle();
                        bundle.putString(Count.JUMP_NAME,Count.JUMP_MAIN);
                        YPPreference.newInstance().setLogin(false);
                        ((BaseActivity)view.getBaseActivity()).startActivityEx(LoginActivity.class,bundle);
                    }
                }
                view.showToast(data.error.message);
                onFail(view,null!=data.error?data.error.code.hashCode():404,data,null);
            }
        }
    }

    /**
     * 处理数据
     *
     * @param view 视图
     * @param data 返回数据
     */
    private void processData(BaseView view, BaseModuleApiResult data) {
        // 通用的处理
        view.hideLoading();
        if (view instanceof BaseYpFreshActivity) {
            ((BaseYpFreshActivity) view).hideLoading();
        } else if (view instanceof BaseYpFreshFragment) {
            ((BaseYpFreshFragment) view).hideLoading();
        }
    }

    /**
     * 回调
     *
     * @param view 视图
     * @param data 数据
     * @note 此回调的view，一定是没有被释放的，可不做判断，直接使用
     */
    public abstract void onSuccess(T view, R data);

    /**
     * 请求错误回调
     *
     * @param view 视图
     * @param code 错误码
     * @param data 数据
     * @param onRetryClickListener 重试点击监听
     */
    public void onFail(T view, int code, R data, final OnRetryClickListener onRetryClickListener) {
        if (null != data) {
            view.showToast(data.error.message);
        } else {
            view.showToast("服务器数据异常");
        }

    }

    /**
     * 请求错误回调
     *
     * @param view 视图
     * @param code 错误码
     * @param data 数据
     */
    @Deprecated
    public void onFail(T view, int code, R data) {
        if (null != data) {
//            view.showToast(data.getErrorMessage());
        } else {
//            view.showToast("服务器数据异常");
        }
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
