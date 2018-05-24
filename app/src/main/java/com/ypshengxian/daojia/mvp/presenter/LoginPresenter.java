package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ILoginContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.LoginBean;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * Created by admin on 2018/3/19.
 *
 * @author Yan
 */

public class LoginPresenter extends BasePresenter<ILoginContract.View> implements ILoginContract.Presenter {

	@Override
	public void requestData(String userPhone, String code,String smsToken) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.loginBySms(userPhone, code,smsToken)
				.compose(((BaseFragment) getView()).<LoginBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<ILoginContract.View, LoginBean>(this) {
					@Override
					public void onSuccess(ILoginContract.View view, LoginBean data) {
						view.hideLoading();
						view.onRequestData(true, data);

					}

					@Override
					public void onFail(ILoginContract.View view, int code, LoginBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.hideLoading();
						view.onRequestData(false, null);
					}
				}, new ModuleErrorAction<ILoginContract.View>(this) {

					@Override
					public void onError(ILoginContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.hideLoading();
						view.onRequestData(false, null);
					}
				});
	}

	@Override
	public void requestCode(Map<String, String> map) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getSmsSend(map)
				.compose(((BaseFragment) getView()).<SmsBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<ILoginContract.View, SmsBean>(this) {
					@Override
					public void onSuccess(ILoginContract.View view, SmsBean data) {
						view.responseCode(true, data);
						getView().hideLoading();
					}

					@Override
					public void onFail(ILoginContract.View view, int code, SmsBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.responseCode(false, null);
						getView().hideLoading();
					}
				}, new ModuleErrorAction<ILoginContract.View>(this) {
					@Override
					public void onError(ILoginContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.responseCode(false, null);
						getView().hideLoading();
					}
				});
	}

	@Override
	public void login(String phone, String pass) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.login(phone, pass)
				.compose(((BaseFragment) getView()).<LoginBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<ILoginContract.View, LoginBean>(this) {
					@Override
					public void onSuccess(ILoginContract.View view, LoginBean data) {
						view.hideLoading();
						view.onLogin(true, data);

					}

					@Override
					public void onFail(ILoginContract.View view, int code, LoginBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.hideLoading();
						view.onLogin(false, null);
					}
				}, new ModuleErrorAction<ILoginContract.View>(this) {

					@Override
					public void onError(ILoginContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.hideLoading();
						view.onLogin(false, null);
					}
				});
	}

	@Override
	public void otherLogin(String oauthCode, String channel) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getLoginBind(oauthCode, channel)
				.compose(((BaseFragment) getView()).<LoginBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<ILoginContract.View, LoginBean>(this) {
					@Override
					public void onSuccess(ILoginContract.View view, LoginBean data) {
						view.onLogin(true, data);
						view.hideLoading();

					}

					@Override
					public void onFail(ILoginContract.View view, int code, LoginBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onLogin(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<ILoginContract.View>(this) {

					@Override
					public void onError(ILoginContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onLogin(false, null);
						view.hideLoading();
					}
				});
	}


}
