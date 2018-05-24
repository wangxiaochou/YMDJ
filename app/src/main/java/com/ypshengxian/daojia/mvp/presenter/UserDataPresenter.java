package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IUserDataContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserProfileBean;

/**
 * @author ZSH
 * @create 2018/3/21
 * @Describe
 */

public class UserDataPresenter extends BasePresenter<IUserDataContract.View> implements IUserDataContract.Presenter {
	@Override
	public void requestData() {

	}

	@Override
	public void uploadAvatar(String fileByte) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.uploadAvatar(fileByte)
				.compose(((BaseActivity) getView()).<UploadBean>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IUserDataContract.View, UploadBean>(this) {
					@Override
					public void onSuccess(IUserDataContract.View view, UploadBean data) {
						view.onUploadAvatar(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IUserDataContract.View view, int code, UploadBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<IUserDataContract.View>(this) {
					@Override
					public void onError(IUserDataContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				});
	}

	@Override
	public void updateUserProfile(String name, String gender,String birthday, String id) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.updateUserProfile(name, gender,birthday, id)
				.compose(((BaseActivity) getView()).<UserProfileBean>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IUserDataContract.View, UserProfileBean>(this) {
					@Override
					public void onSuccess(IUserDataContract.View view, UserProfileBean data) {
						view.onUpdateUserProfile(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IUserDataContract.View view, int code, UserProfileBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onUpdateUserProfile(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<IUserDataContract.View>(this) {
					@Override
					public void onError(IUserDataContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onUpdateUserProfile(false, null);
						view.hideLoading();
					}
				});
	}
}
