package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IIntegralDetailContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
import com.ypshengxian.daojia.network.bean.UserPointFlowBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class IntegralDetailPresenter extends BasePresenter<IIntegralDetailContract.View> implements IIntegralDetailContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getUserPointFlow(map)
                .compose(((BaseActivity) getView()).<UserPointFlowBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IIntegralDetailContract.View, UserPointFlowBean>(this) {
                    @Override
                    public void onSuccess(IIntegralDetailContract.View view, UserPointFlowBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IIntegralDetailContract.View view, int code, UserPointFlowBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IIntegralDetailContract.View>(this) {

                    @Override
                    public void onError(IIntegralDetailContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
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
                .subscribe(new ModuleSuccessAction<IIntegralDetailContract.View, UserCoinBean>(this) {

                    @Override
                    public void onSuccess(IIntegralDetailContract.View view, UserCoinBean data) {
                        view.hideLoading();
                        view.onGetUserCoin(true, data);
                    }

                    @Override
                    public void onFail(IIntegralDetailContract.View view, int code, UserCoinBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.hideLoading();
                        view.onGetUserCoin(false, null);
                    }
                }, new ModuleErrorAction<IIntegralDetailContract.View>(this) {
                    @Override
                    public void onError(IIntegralDetailContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.hideLoading();
                        view.onGetUserCoin(false, null);
                    }
                });
    }
}
