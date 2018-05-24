package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ICartContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;

import java.util.Map;

/**
 * 购物车
 *
 * @author Yan
 * @date 2018-03-29
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CartPresenter  extends BasePresenter<ICartContract.View> implements ICartContract.Presenter{
    @Override
    public void requestData() {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getCart()
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICartContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICartContract.View view, AddCartBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(ICartContract.View view, int code, AddCartBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();

                    }
                }, new ModuleErrorAction<ICartContract.View>(this) {

                    @Override
                    public void onError(ICartContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }

                });

    }

    @Override
    public void deletedCart(Map<String, String> map) {
        YpFreshApi.newInstance().getSSService()
                .deletedCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICartContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICartContract.View view, AddCartBean data) {
                        view.onDeletedCart(true, data);
                    }

                    @Override
                    public void onFail(ICartContract.View view, int code, AddCartBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onDeletedCart(false, null);

                    }
                }, new ModuleErrorAction<ICartContract.View>(this) {

                    @Override
                    public void onError(ICartContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onDeletedCart(false, null);
                    }

                });
    }

    @Override
    public void addCart(Map<String, String> map) {
        YpFreshApi.newInstance().getSSService()
                .addCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICartContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICartContract.View view, AddCartBean data) {
                        view.onAddCart(true, data);
                    }

                    @Override
                    public void onFail(ICartContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onAddCart(false, null);

                    }
                }, new ModuleErrorAction<ICartContract.View>(this) {

                    @Override
                    public void onError(ICartContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onAddCart(false, null);
                    }

                });
    }

    @Override
    public void updateCart(Map<String, String> map) {
        YpFreshApi.newInstance().getSSService()
                .updateCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICartContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICartContract.View view, AddCartBean data) {
                        view.onUpdateCart(true, data);
                    }

                    @Override
                    public void onFail(ICartContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onUpdateCart(false, null);

                    }
                }, new ModuleErrorAction<ICartContract.View>(this) {

                    @Override
                    public void onError(ICartContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onUpdateCart(false, null);
                    }

                });
    }

    @Override
    public void batchUpdateCart(Map<String, String> map) {
        YpFreshApi.newInstance().getSSService()
                .batchUpdateCart(map)
                .compose(((BaseFragment) getView()).<AddCartBean>applySchedulers(FragmentEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ICartContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(ICartContract.View view, AddCartBean data) {
                        view.onBatchUpdateCart(true, data);
                    }

                    @Override
                    public void onFail(ICartContract.View view, int code, AddCartBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onBatchUpdateCart(false, null);

                    }
                }, new ModuleErrorAction<ICartContract.View>(this) {

                    @Override
                    public void onError(ICartContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onBatchUpdateCart(false, null);
                    }

                });
    }
}
