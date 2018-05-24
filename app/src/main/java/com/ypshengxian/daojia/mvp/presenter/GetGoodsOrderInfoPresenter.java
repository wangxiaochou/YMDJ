package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IGetGoodsOrderinfoContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.LineItemBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class GetGoodsOrderInfoPresenter extends BasePresenter<IGetGoodsOrderinfoContract.View> implements IGetGoodsOrderinfoContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getOrder(map)
                .compose(((BaseActivity) getView()).<LineItemBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IGetGoodsOrderinfoContract.View, LineItemBean>(this) {
                    @Override
                    public void onSuccess(IGetGoodsOrderinfoContract.View view, LineItemBean data) {
                        view.onResponseData(true, data);
                        view.hideLoading();

                    }

                    @Override
                    public void onFail(IGetGoodsOrderinfoContract.View view, int code, LineItemBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<IGetGoodsOrderinfoContract.View>(this) {

                    @Override
                    public void onError(IGetGoodsOrderinfoContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false, null);
                        view.hideLoading();
                    }
                });
    }
}
