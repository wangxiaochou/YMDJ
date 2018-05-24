package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IPayWayContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.PayBean;


/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */
public class PayWayPresenter extends BasePresenter<IPayWayContract.View> implements IPayWayContract.Presenter {
    @Override
    public void requestData(String gateway, String orderSn, String type, String coinAmount, String payPassword) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .payOrder(gateway, orderSn, type, coinAmount, payPassword)
                .compose(((BaseActivity) getView()).<PayBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IPayWayContract.View, PayBean>(this) {

                    @Override
                    public void onSuccess(IPayWayContract.View view, PayBean data) {
                        view.hideLoading();
                        view.onResponseData(true, data);
                    }

                    @Override
                    public void onFail(IPayWayContract.View view, int code, PayBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.hideLoading();
                        view.onResponseData(false, null);
                    }
                }, new ModuleErrorAction<IPayWayContract.View>(this) {
                    @Override
                    public void onError(IPayWayContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.hideLoading();
                        view.onResponseData(false, null);
                    }
                });
    }

}
