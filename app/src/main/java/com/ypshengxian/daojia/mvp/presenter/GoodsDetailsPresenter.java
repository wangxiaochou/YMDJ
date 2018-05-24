package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IGoodsDetailsContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.GoodsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe
 */

public class GoodsDetailsPresenter extends BasePresenter<IGoodsDetailsContract.View> implements IGoodsDetailsContract.Presenter {
	@Override
	public void requestData(String goodsId) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.getGoodsInfo(goodsId)
				.compose(((BaseActivity) getView()).<GoodsBean>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IGoodsDetailsContract.View, GoodsBean>(this) {
					@Override
					public void onSuccess(IGoodsDetailsContract.View view, GoodsBean data) {
						view.onResponseData(true, data);
						getView().hideLoading();
					}

					@Override
					public void onFail(IGoodsDetailsContract.View view, int code, GoodsBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onResponseData(false, null);
						getView().hideLoading();
					}
				}, new ModuleErrorAction<IGoodsDetailsContract.View>(this) {
					@Override
					public void onError(IGoodsDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onResponseData(false, null);
						getView().hideLoading();
					}
				});
	}

	@Override
	public void addCart(Map<String, String> map) {
		getView().showLoading();
		YpFreshApi.newInstance().getSSService()
				.addCart(map)
				.compose(((BaseActivity) getView()).<AddCartBean>applySchedulers(ActivityEvent.DESTROY))
				.subscribe(new ModuleSuccessAction<IGoodsDetailsContract.View, AddCartBean>(this) {
					@Override
					public void onSuccess(IGoodsDetailsContract.View view, AddCartBean data) {
						view.onAddCart(true, data);
						view.hideLoading();
					}

					@Override
					public void onFail(IGoodsDetailsContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
						super.onFail(view, code, data, onRetryClickListener);
						view.onAddCart(false, null);
						view.hideLoading();

					}
				}, new ModuleErrorAction<IGoodsDetailsContract.View>(this) {

					@Override
					public void onError(IGoodsDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
						super.onError(view, throwable, onRetryClickListener);
						view.onAddCart(false, null);
						view.hideLoading();
					}

				});
	}
}
