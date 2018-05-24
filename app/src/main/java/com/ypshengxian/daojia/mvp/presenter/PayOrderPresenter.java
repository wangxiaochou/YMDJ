package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IPayOrderContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.CreateOrderBean;
import com.ypshengxian.daojia.network.bean.PayOrderBean;

import java.util.Map;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class PayOrderPresenter extends BasePresenter<IPayOrderContract.View> implements IPayOrderContract.Presenter {
    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getPayOrder(map)
                .compose(((BaseActivity) getView()).<PayOrderBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IPayOrderContract.View, PayOrderBean>(this) {
                    @Override
                    public void onSuccess(IPayOrderContract.View view, PayOrderBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IPayOrderContract.View view, int code, PayOrderBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IPayOrderContract.View>(this) {

                    @Override
                    public void onError(IPayOrderContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void createOrder(String shopId, String couponId, String packetId,String deliveryTime) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .createOrder(shopId, couponId, packetId,deliveryTime)
                .compose(((BaseActivity) getView()).<CreateOrderBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IPayOrderContract.View,CreateOrderBean>(this) {
                    @Override
                    public void onSuccess(IPayOrderContract.View view, CreateOrderBean data) {
                        view.onCreateOrder(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IPayOrderContract.View view, int code, CreateOrderBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onCreateOrder(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IPayOrderContract.View>(this) {

                    @Override
                    public void onError(IPayOrderContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onCreateOrder(false, null);
                        view.hideLoading();
                    }
                });
    }
}
