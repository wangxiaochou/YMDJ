package com.ypshengxian.daojia.mvp.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IOrderDetailsContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.LineItemBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe
 */

public class OrderDetailsPresenter extends BasePresenter<IOrderDetailsContract.View> implements IOrderDetailsContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getOrder(map)
                .compose(((BaseActivity) getView()).<LineItemBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IOrderDetailsContract.View, LineItemBean>(this) {
                    @Override
                    public void onSuccess(IOrderDetailsContract.View view, LineItemBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();

                    }

                    @Override
                    public void onFail(IOrderDetailsContract.View view, int code, LineItemBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderDetailsContract.View>(this) {

                    @Override
                    public void onError(IOrderDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });

    }

    @Override
    public void closeOrder(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .closeOrder(map)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IOrderDetailsContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(IOrderDetailsContract.View view, BaseModuleApiResult data) {
                        view.onCloseOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IOrderDetailsContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onCloseOrder(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderDetailsContract.View>(this) {

                    @Override
                    public void onError(IOrderDetailsContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        if(TextUtils.equals(Count.ERROR_MESSAGE,throwable.getMessage())){
                            view.onCloseOrder(true, null);
                        }else {
                            view.onCloseOrder(false, null);
                        }
                        view.hideLoading();
                    }
                });

    }

    @Override
    public void refundOrder(String orderSn, String refundAmount,String reason, String[] id ) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .refundOrder(orderSn, refundAmount, reason, id)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IOrderDetailsContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(IOrderDetailsContract.View view, BaseModuleApiResult data) {
                        view.onRefundOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IOrderDetailsContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onRefundOrder(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderDetailsContract.View>(this) {

                    @Override
                    public void onError(IOrderDetailsContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        if(TextUtils.equals(Count.ERROR_MESSAGE,throwable.getMessage())){
                            view.onRefundOrder(true, null);
                        }else {
                            view.onRefundOrder(false, null);
                        }
                        view.hideLoading();
                    }
                });
    }
}
