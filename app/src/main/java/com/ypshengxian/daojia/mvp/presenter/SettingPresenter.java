package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ISettingContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public class SettingPresenter extends BasePresenter<ISettingContract.View> implements ISettingContract.Presenter {
	@Override
	public void requestData() {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getLogout()
				.compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<ISettingContract.View, BaseModuleApiResult>(this) {
					@Override
					public void onSuccess(ISettingContract.View view, BaseModuleApiResult data) {
						view.onResponseData(true, data);
						getView().hideLoading();
					}

					@Override
					public void onFail(ISettingContract.View view, int code, BaseModuleApiResult data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onResponseData(false, null);
						getView().hideLoading();
					}
				}, new ModuleErrorAction<ISettingContract.View>(this) {
					@Override
					public void onError(ISettingContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onResponseData(false, null);
						getView().hideLoading();
					}
				});
	}
}
