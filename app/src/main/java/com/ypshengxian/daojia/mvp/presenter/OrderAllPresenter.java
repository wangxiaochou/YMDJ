package com.ypshengxian.daojia.mvp.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IOrderAllContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.MyOrderBean;

import java.util.Map;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class OrderAllPresenter extends BasePresenter<IOrderAllContract.View> implements IOrderAllContract.Presenter {
    @Override
    public void getMyOrder(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getMyOrder(map)
                .compose(((BaseActivity) getView()).<MyOrderBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IOrderAllContract.View, MyOrderBean>(this) {
                    @Override
                    public void onSuccess(IOrderAllContract.View view, MyOrderBean data) {
                        view.onGetMyOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IOrderAllContract.View view, int code, MyOrderBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onGetMyOrder(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderAllContract.View>(this) {

                    @Override
                    public void onError(IOrderAllContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onGetMyOrder(false, null);
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
                .subscribe(new ModuleSuccessAction<IOrderAllContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(IOrderAllContract.View view, BaseModuleApiResult data) {
                        view.onCloseOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IOrderAllContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onCloseOrder(false, data);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderAllContract.View>(this) {

                    @Override
                    public void onError(IOrderAllContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                         if(TextUtils.equals(Count.ERROR_MESSAGE,throwable.getMessage())){
                             view.onCloseOrder(true,null);
                         }else {
                             view.onCloseOrder(false, null);
                         }
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void refundOrder( String orderSn, String refundAmount,String reason, String[] id) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .refundOrder(orderSn, refundAmount, reason, id)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IOrderAllContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(IOrderAllContract.View view, BaseModuleApiResult data) {
                        view.onRefundOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IOrderAllContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onRefundOrder(false, data);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IOrderAllContract.View>(this) {

                    @Override
                    public void onError(IOrderAllContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
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
