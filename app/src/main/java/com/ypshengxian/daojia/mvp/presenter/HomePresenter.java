package com.ypshengxian.daojia.mvp.presenter;

import android.view.View;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;
import com.ypshengxian.daojia.mvp.contract.IHomeContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.HomeBean;
import com.ypshengxian.daojia.network.bean.ShopBean;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class HomePresenter extends BasePresenter<IHomeContract.View> implements IHomeContract.Presenter {
    @Override
    public void requestData() {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getHomePage()
                .compose(((BaseFragment) getView()).<HomeBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IHomeContract.View, HomeBean>(this) {
                    @Override
                    public void onSuccess(IHomeContract.View view, HomeBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                        ((BaseYpFreshFragment)view).showStatusHide();

                    }

                    @Override
                    public void onFail(IHomeContract.View view, int code, HomeBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                        ((BaseYpFreshFragment)view).showStatusLoadFail(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestData();
                            }
                        });

                    }
                }, new ModuleErrorAction<IHomeContract.View>(this) {
                    @Override
                    public void onError(IHomeContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                        ((BaseYpFreshFragment)view).showStatusLoadFail(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestData();
                            }
                        });

                    }
                });

    }

    @Override
    public void addCart(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .addCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IHomeContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(IHomeContract.View view, AddCartBean data) {
                        view.onAddCart(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IHomeContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();

                    }
                }, new ModuleErrorAction<IHomeContract.View>(this) {

                    @Override
                    public void onError(IHomeContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();
                    }

                });

    }

    @Override
    public void getShop() {
        IHomeContract.View view=getView();
        if(null!=view) {
            view.showLoading();
            YpFreshApi.newInstance().getSSService()
                    .getShop()
                    .compose(((BaseFragment) getView()).<List<ShopBean>>applySchedulers(FragmentEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<ShopBean>>() {
                        @Override
                        public void onCompleted() {
                            view.onGetShop(false, null);
                            view.hideLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.onGetShop(false, null);
                            view.hideLoading();
                        }

                        @Override
                        public void onNext(List<ShopBean> shopBeans) {
                            view.onGetShop(true, shopBeans);
                            view.hideLoading();
                        }
                    });
        }
    }


}
