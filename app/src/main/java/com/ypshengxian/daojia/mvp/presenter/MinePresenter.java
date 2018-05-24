package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IMineContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.OrderBean;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;

import rx.Observable;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public class MinePresenter extends BasePresenter<IMineContract.View> implements IMineContract.Presenter {


	@Override
	public void requestData() {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getMe()
				.compose(((BaseFragment) getView()).<UserBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IMineContract.View, UserBean>(this) {

					@Override
					public void onSuccess(IMineContract.View view, UserBean data) {
						view.hideLoading();
						view.onResponseData(true,data);
					}

					@Override
					public void onFail(IMineContract.View view, int code, UserBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.hideLoading();
						view.onResponseData(false,null);
					}
				}, new ModuleErrorAction<IMineContract.View>(this) {
					@Override
					public void onError(IMineContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.hideLoading();
						view.onResponseData(false,null);
					}
				});

	}

	@Override
	public void getUserCoin() {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getUserCoin()
				.compose(((BaseFragment) getView()).<UserCoinBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IMineContract.View, UserCoinBean>(this) {

					@Override
					public void onSuccess(IMineContract.View view, UserCoinBean data) {
						view.hideLoading();
						view.onGetUserCoin(true,data);
					}

					@Override
					public void onFail(IMineContract.View view, int code, UserCoinBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.hideLoading();
						view.onGetUserCoin(false,null);
					}
				}, new ModuleErrorAction<IMineContract.View>(this) {
					@Override
					public void onError(IMineContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.hideLoading();
						view.onGetUserCoin(false,null);
					}
				});
	}

	@Override
	public void getOrder() {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getOrderCount()
				.compose(((BaseFragment) getView()).<OrderBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IMineContract.View, OrderBean>(this) {

					@Override
					public void onSuccess(IMineContract.View view, OrderBean data) {
						view.hideLoading();
						view.onGetOrder(true,data);
					}

					@Override
					public void onFail(IMineContract.View view, int code, OrderBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.hideLoading();
						view.onGetOrder(false,null);
					}
				}, new ModuleErrorAction<IMineContract.View>(this) {
					@Override
					public void onError(IMineContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.hideLoading();
						view.onGetOrder(false,null);
					}
				});
	}


	/**
	 * 获得用户数据
	 *
	 * @return 观察者
	 */
	private Observable<UserBean> requestUseData() {
		return YpFreshApi.newInstance().getSSService()
				.getMe()
				.compose(((BaseFragment) getView()).<UserBean>applySchedulers(FragmentEvent.DESTROY));
	}

	/**
	 * 获得用户账户数据
	 *
	 * @return 观察者
	 */
	private Observable<UserCoinBean> requestUserCoinData() {
		return YpFreshApi.newInstance().getSSService()
				.getUserCoin()
				.compose(((BaseFragment) getView()).<UserCoinBean>applySchedulers(FragmentEvent.DESTROY));



	}


	@Override
	public void uploadAvatar(String fileByte) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.uploadAvatar(fileByte)
				.compose(((BaseFragment) getView()).<UploadBean>applySchedulers(FragmentEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IMineContract.View, UploadBean>(this) {
					@Override
					public void onSuccess(IMineContract.View view, UploadBean data) {
						view.onUploadAvatar(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IMineContract.View view, int code, UploadBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<IMineContract.View>(this) {
					@Override
					public void onError(IMineContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				});
	}
}
