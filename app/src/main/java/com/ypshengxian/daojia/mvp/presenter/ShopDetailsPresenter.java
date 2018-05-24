package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IShopDetailsContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.TopBean;

import java.util.Map;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-28
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ShopDetailsPresenter extends BasePresenter<IShopDetailsContract.View> implements IShopDetailsContract.Presenter {
    @Override
    public void requestData(String id) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getTop(id)
                .compose(((BaseActivity) getView()).<TopBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IShopDetailsContract.View, TopBean>(this) {
                    @Override
                    public void onSuccess(IShopDetailsContract.View view, TopBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IShopDetailsContract.View view, int code, TopBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IShopDetailsContract.View>(this) {

                    @Override
                    public void onError(IShopDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void getNewSales(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getNewSales(map)
                .compose(((BaseActivity) getView()).<TopBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IShopDetailsContract.View, TopBean>(this) {
                    @Override
                    public void onSuccess(IShopDetailsContract.View view, TopBean data) {
                        view.onGetNewSales(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IShopDetailsContract.View view, int code, TopBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onGetNewSales(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IShopDetailsContract.View>(this) {

                    @Override
                    public void onError(IShopDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onGetNewSales(false, null);
                        view.hideLoading();
                    }
                });

    }

    @Override
    public void addCart(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .addCart(map)
                .compose(((BaseActivity) getView()).<AddCartBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IShopDetailsContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(IShopDetailsContract.View view, AddCartBean data) {
                        view.onAddCart(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IShopDetailsContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();

                    }
                }, new ModuleErrorAction<IShopDetailsContract.View>(this) {

                    @Override
                    public void onError(IShopDetailsContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();
                    }

                });
    }
}
