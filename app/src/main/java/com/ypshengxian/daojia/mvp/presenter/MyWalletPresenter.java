package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IMyWalletContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.UserCashFlowBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class MyWalletPresenter extends BasePresenter<IMyWalletContract.View> implements IMyWalletContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getUserCashFlow(map)
                .compose(((BaseActivity) getView()).<UserCashFlowBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IMyWalletContract.View, UserCashFlowBean>(this) {
                    @Override
                    public void onSuccess(IMyWalletContract.View view, UserCashFlowBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IMyWalletContract.View view, int code, UserCashFlowBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IMyWalletContract.View>(this) {

                    @Override
                    public void onError(IMyWalletContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });

    }


    @Override
    public void getUserCoin() {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getUserCoin()
                .compose(((BaseActivity) getView()).<UserCoinBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IMyWalletContract.View, UserCoinBean>(this) {

                    @Override
                    public void onSuccess(IMyWalletContract.View view, UserCoinBean data) {
                        view.hideLoading();
                        view.onGetUserCoin(true, data);
                    }

                    @Override
                    public void onFail(IMyWalletContract.View view, int code, UserCoinBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.hideLoading();
                        view.onGetUserCoin(false, null);
                    }
                }, new ModuleErrorAction<IMyWalletContract.View>(this) {
                    @Override
                    public void onError(IMyWalletContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.hideLoading();
                        view.onGetUserCoin(false, null);
                    }
                });
    }
}
