package com.ypshengxian.daojia.mvp.presenter;

import android.view.View;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;
import com.ypshengxian.daojia.mvp.contract.ICategoryContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;
import com.ypshengxian.daojia.network.bean.CateGoryBean;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-27
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CategoryPresenter extends BasePresenter<ICategoryContract.View> implements ICategoryContract.Presenter {
    @Override
    public void requestData() {
        ICategoryContract.View view=getView();
        if(null!=view) {
            view.showLoading();
            YpFreshApi.newInstance().getSSService()
                    .getCategory()
                    .compose(((BaseFragment) getView()).<List<CateGoryBean>>applySchedulers(FragmentEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CateGoryBean>>() {
                        @Override
                        public void onCompleted() {
                            view.hideLoading();
                            view.onResponseData(false, null);

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.hideLoading();
                            view.onResponseData(false, null);
                            ((BaseYpFreshFragment) view).showStatusLoadFail(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestData();
                                }
                            });
                        }

                        @Override
                        public void onNext(List<CateGoryBean> classCategoryBeans) {
                            view.onResponseData(true, classCategoryBeans);
                            view.hideLoading();
                            ((BaseYpFreshFragment) view).showStatusHide();
                        }
                    });
        }

    }

    @Override
    public void getGoods(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getGoods(map)
                .compose(((BaseFragment) getView()).<AllGoodsBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ModuleSuccessAction<ICategoryContract.View, AllGoodsBean>(this) {
                    @Override
                    public void onSuccess(ICategoryContract.View view, AllGoodsBean data) {
                        view.onGetGoods(true,data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(ICategoryContract.View view, int code, AllGoodsBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onGetGoods(false,null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<ICategoryContract.View>(this) {
                    @Override
                    public void onError(ICategoryContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onGetGoods(false,null);
                        view.hideLoading();
                    }
                });

    }

    @Override
    public void addCart(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .addCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICategoryContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICategoryContract.View view, AddCartBean data) {
                        view.onAddCart(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(ICategoryContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();

                    }
                }, new ModuleErrorAction<ICategoryContract.View>(this) {

                    @Override
                    public void onError(ICategoryContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();
                    }

                });
    }
}
