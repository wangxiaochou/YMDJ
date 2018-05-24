package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IApplyAfterSalesContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.UploadBean;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class ApplyAfterSalesPresenter extends BasePresenter<IApplyAfterSalesContract.View> implements IApplyAfterSalesContract.Presenter {
	@Override
	public void requestData(String orderSn, String refundAmount,String reason, String[] id ) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.refundOrder(orderSn, refundAmount, reason, id)
				.compose(((BaseActivity) getView()).<BaseModuleApiResult>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IApplyAfterSalesContract.View, BaseModuleApiResult>(this) {
					@Override
					public void onSuccess(IApplyAfterSalesContract.View view, BaseModuleApiResult data) {
						view.onResponseData(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IApplyAfterSalesContract.View view, int code, BaseModuleApiResult data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onResponseData(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<IApplyAfterSalesContract.View>(this) {

					@Override
					public void onError(IApplyAfterSalesContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onResponseData(false, null);
						view.hideLoading();
					}
				});


	}

	@Override
	public void uploadAvatar(String fileByte) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.uploadAvatar(fileByte)
				.compose(((BaseActivity) getView()).<UploadBean>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IApplyAfterSalesContract.View, UploadBean>(this) {
					@Override
					public void onSuccess(IApplyAfterSalesContract.View view, UploadBean data) {
						view.onUploadAvatar(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IApplyAfterSalesContract.View view, int code, UploadBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				}, new ModuleErrorAction<IApplyAfterSalesContract.View>(this) {
					@Override
					public void onError(IApplyAfterSalesContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onUploadAvatar(false, null);
						view.hideLoading();
					}
				});
	}
}
