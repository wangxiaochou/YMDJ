package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ILoginActivityContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.base.ModuleErrorAction;
import com.ypshengxian.daojia.network.base.ModuleSuccessAction;
import com.ypshengxian.daojia.network.bean.LoginBean;

/**
 * 微信登录
 *
 * @author Yan
 * @date 2018-03-30
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class LoginActivityPresenter extends BasePresenter<ILoginActivityContract.View> implements ILoginActivityContract.Presenter {
    @Override
    public void wxLogin(String oauthCode,String channel) {
        getView().showLoading();
        YpFreshApi.newInstance().getSSService()
                .getLoginBind(oauthCode, channel)
                .compose(((BaseActivity) getView()).<LoginBean>applySchedulers(ActivityEvent.DESTROY))
                .subscribe(new ModuleSuccessAction<ILoginActivityContract.View, LoginBean>(this) {
                    @Override
                    public void onSuccess(ILoginActivityContract.View view, LoginBean data) {
                        view.onWxLogin(true, data);
                        view.hideLoading();

                    }

                    @Override
                    public void onFail(ILoginActivityContract.View view, int code, LoginBean data, ModuleSuccessAction.OnRetryClickListener onRetryClickListener) {
                        super.onFail(view, code, data, onRetryClickListener);
                        view.onWxLogin(false, null);
                        view.hideLoading();
                    }
                }, new ModuleErrorAction<ILoginActivityContract.View>(this) {

                    @Override
                    public void onError(ILoginActivityContract.View view, Throwable throwable, ModuleErrorAction.OnRetryClickListener onRetryClickListener) {
                        super.onError(view, throwable, onRetryClickListener);
                        view.onWxLogin(false, null);
                        view.hideLoading();
                    }
                });
    }
}
