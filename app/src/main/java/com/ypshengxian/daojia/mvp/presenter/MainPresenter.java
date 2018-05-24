package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IMainContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.OrderBean;

/**
 * 网络活动
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class MainPresenter  extends BasePresenter<IMainContract.View> implements IMainContract.Presenter {
    @Override
    public void requestData() {
        YpFreshApi.newInstance().getSSService()
                .getOrderCount()
                .compose(((BaseActivity) getView()).<OrderBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<IMainContract.View, OrderBean>(this) {

                    @Override
                    public void onSuccess(IMainContract.View view, OrderBean data) {
                        view.onResponseData(true,data);
                    }

                    @Override
                    public void onFail(IMainContract.View view, int code, OrderBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onResponseData(false,null);
                    }
                }, new ModuleErrorAction<IMainContract.View>(this) {
                    @Override
                    public void onError(IMainContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onResponseData(false,null);
                    }
                });

    }
}
