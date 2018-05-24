package com.ypshengxian.daojia.mvp.presenter;

import android.text.TextUtils;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.count.Count;
import com.ypshengxian.daojia.mvp.contract.IBindingPhoneContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/25
 * @Describe
 */

public class BindingPhonePresenter extends BasePresenter<IBindingPhoneContract.View> implements IBindingPhoneContract.Presenter {

    @Override
    public void sendSms(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getSmsSend(map)
                .compose(((BaseActivity) getView()).<SmsBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IBindingPhoneContract.View, SmsBean>(this) {
                    @Override
                    public void onSuccess(IBindingPhoneContract.View view, SmsBean data) {
                        view.onSendSms(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IBindingPhoneContract.View view, int code, SmsBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onSendSms(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IBindingPhoneContract.View>(this) {
                    @Override
                    public void onError(IBindingPhoneContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onSendSms(false, null);
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void bindPhone(String phone, String code, String smsToken) {
        YpFreshApi.newInstance().getSSService()
                .bindPhone(code, phone, smsToken)
                .compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IBindingPhoneContract.View, BaseModuleApiResult>(this) {
                    @Override
                    public void onSuccess(IBindingPhoneContract.View view, BaseModuleApiResult data) {
                        view.onBindPhone(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IBindingPhoneContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onBindPhone(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IBindingPhoneContract.View>(this) {
                    @Override
                    public void onError(IBindingPhoneContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        if (TextUtils.equals(Count.ERROR_MESSAGE, throwable.getMessage())) {
                            view.onBindPhone(true, null);
                        } else {
                            view.onBindPhone(false, null);
                        }
                        view.hideLoading();
                    }
                });
    }
}
