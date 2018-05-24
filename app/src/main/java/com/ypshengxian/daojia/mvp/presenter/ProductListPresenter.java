package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IProductListContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;

import java.util.Map;

/**
 * 商品列表
 *
 * @author Yan
 * @date 2018-03-30
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ProductListPresenter extends BasePresenter<IProductListContract.View> implements IProductListContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getGoods(map)
                .compose(((BaseActivity) getView()).<AllGoodsBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IProductListContract.View, AllGoodsBean>(this) {
                    @Override
                    public void onSuccess(IProductListContract.View view, AllGoodsBean data) {
                        view.onResponseData(true,data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IProductListContract.View view, int code, AllGoodsBean data, OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false,null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IProductListContract.View>(this) {
                    @Override
                    public void onError(IProductListContract.View view, Throwable throwable, OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false,null);
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
                .subscribe(new ModuleSuccessAction<IProductListContract.View, AddCartBean>(this) {
                    @Override
                    public void onSuccess(IProductListContract.View view, AddCartBean data) {
                        view.onAddCart(true, data);
                        view.hideLoading();
                    }

                    @Override
                    public void onFail(IProductListContract.View view, int code, AddCartBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();

                    }
                }, new ModuleErrorAction<IProductListContract.View>(this) {

                    @Override
                    public void onError(IProductListContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onAddCart(false, null);
                        view.hideLoading();
                    }

                });
    }
}
