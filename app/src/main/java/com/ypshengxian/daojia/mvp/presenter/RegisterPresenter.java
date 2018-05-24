package com.ypshengxian.daojia.mvp.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IRegisterContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.RegisterBean;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * Created by YAN
 *
 * @author lenovo
 */

public class RegisterPresenter extends BasePresenter<IRegisterContract.View> implements IRegisterContract.Presenter {
    @Override
    public void bindPhone(String password, String nickname, String phone, String smsCode, String registeredWay,String smsToken) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getRegister(password, nickname, phone, smsCode, registeredWay,smsToken)
                .compose(((BaseActivity) getView()).<RegisterBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IRegisterContract.View, RegisterBean>(this) {

                    @Override
                    public void onSuccess(IRegisterContract.View view, RegisterBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IRegisterContract.View view, int code, RegisterBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IRegisterContract.View>(this) {
                    @Override
                    public void onError(IRegisterContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void resetBySms(String password, String phone, String smsCode,String smsToken) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .resetBySms(password, phone, smsCode,smsToken)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IRegisterContract.View, BaseModuleApiResult>(this) {

                    @Override
                    public void onSuccess(IRegisterContract.View view, BaseModuleApiResult data) {
                        view.onResponseResetBySms(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IRegisterContract.View view, int code, BaseModuleApiResult data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseResetBySms(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IRegisterContract.View>(this) {
                    @Override
                    public void onError(IRegisterContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        if (TextUtils.equals(Count.ERROR_MESSAGE, throwable.getMessage())) {
                            view.onResponseResetBySms(true, null);
                        } else {
                            view.onResponseResetBySms(false, null);
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
                .subscribe(new ModuleSuccessAction<IRegisterContract.View, SmsBean>(this) {
                    @Override
                    public void onSuccess(IRegisterContract.View view, SmsBean data) {
                        view.onResponseCode(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IRegisterContract.View view, int code, SmsBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseCode(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IRegisterContract.View>(this) {
                    @Override
                    public void onError(IRegisterContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseCode(false, null);
                        view.hideLoading();
                    }
                });
    }
}
