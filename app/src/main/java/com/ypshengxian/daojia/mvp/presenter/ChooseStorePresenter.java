package com.ypshengxian.daojia.mvp.presenter;

import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseYpFreshActivity;
import com.ypshengxian.daojia.mvp.contract.IChooseStoreContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.bean.CityBean;
import com.ypshengxian.daojia.network.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author ZSH
 * @create 2018/3/21
 * @Describe
 */

public class ChooseStorePresenter extends BasePresenter<IChooseStoreContract.View> implements IChooseStoreContract.Presenter {

	@Override
	public void requestData() {
		IChooseStoreContract.View view=getView();
		if(null!=view) {
			view.showLoading();
			YpFreshApi.newInstance().getSSService()
					.getShop()
					.compose(((BaseActivity) getView()).<ArrayList<ShopBean>>applySchedulers(ActivityEvent.DESTROY))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Subscriber<ArrayList<ShopBean>>() {
						@Override
						public void onCompleted() {
							view.onResponseData(false, null);
							view.hideLoading();
						}

						@Override
						public void onError(Throwable e) {
							view.onResponseData(false, null);
							view.hideLoading();
						}

						@Override
						public void onNext(ArrayList<ShopBean> shopBeans) {
							view.onResponseData(true, shopBeans);
							view.hideLoading();
						}
					});
		}
	}

	@Override
	public void getCity() {
		IChooseStoreContract.View view=getView();
		if(null!=view) {
			view.showLoading();
			YpFreshApi.newInstance().getSSService()
					.getCitys()
					.compose(((BaseActivity) getView()).<ArrayList<CityBean>>applySchedulers(ActivityEvent.DESTROY))
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Subscriber<ArrayList<CityBean>>() {
						@Override
						public void onCompleted() {
							view.hideLoading();
							view.onGetCity(false, null);
						}

						@Override
						public void onError(Throwable e) {
							view.hideLoading();
							view.onGetCity(false, null);
							((BaseYpFreshActivity)getView()).showStatusLoadFail(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									getCity();
								}
							});
						}

						@Override
						public void onNext(ArrayList<CityBean> cityBeans) {
							view.hideLoading();
							view.onGetCity(true, cityBeans);
							((BaseYpFreshActivity)view).showStatusHide();
						}
					});
		}

	}

	/**
	 * 获得城市列表
	 *
	 * @return 观察者
	 */
	private Observable<ArrayList<CityBean>> requestCityData() {
		return YpFreshApi.newInstance().getSSService()
				.getCitys()
				.compose(((BaseActivity) getView()).<ArrayList<CityBean>>applySchedulers(ActivityEvent.DESTROY));
	}

	/**
	 * 获得门店列表
	 *
	 * @return 观察者
	 */
	private Observable<List<ShopBean>> requestShopData() {

		return YpFreshApi.newInstance().getSSService()
				.getShop()
				.compose(((BaseActivity) getView()).<List<ShopBean>>applySchedulers(ActivityEvent.DESTROY));



	}
}
