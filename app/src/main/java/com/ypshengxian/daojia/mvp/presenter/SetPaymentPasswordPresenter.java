package com.ypshengxian.daojia.mvp.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.ISetPaymentPasswordContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class SetPaymentPasswordPresenter extends BasePresenter<ISetPaymentPasswordContract.View> implements ISetPaymentPasswordContract.Presenter {

    @Override
    public void requestData(String phone, String smsCode, String payPassword,String smsToken) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .setUserPayPassword(phone, smsCode, payPassword,smsToken)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ISetPaymentPasswordContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(ISetPaymentPasswordContract.View view, BaseModuleApiResult data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(ISetPaymentPasswordContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<ISetPaymentPasswordContract.View>(this) {
                    @Override
                    public void onError(ISetPaymentPasswordContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        if(TextUtils.equals(Count.ERROR_MESSAGE,throwable.getMessage())){
                            view.onResponseData(true, null);
                        }else {
                            view.onResponseData(false, null);
                        }
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void getCode(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getSmsSend(map)
                .compose(((BaseActivity) getView()).<SmsBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ISetPaymentPasswordContract.View, SmsBean>(this) {
                    @Override
                    public void onSuccess(ISetPaymentPasswordContract.View view, SmsBean data) {
                        view.onResponseCode(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(ISetPaymentPasswordContract.View view, int code, SmsBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseCode(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<ISetPaymentPasswordContract.View>(this) {
                    @Override
                    public void onError(ISetPaymentPasswordContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseCode(false, null);
                        view.hideLoading();
                    }
                });
    }
}
