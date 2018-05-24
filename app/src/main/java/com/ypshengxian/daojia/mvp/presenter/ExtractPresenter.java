package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.base.BaseFragment;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IExtractContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.bean.CodeBean;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-05
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ExtractPresenter extends BasePresenter<IExtractContract.View> implements IExtractContract.Presenter {
    @Override
    public void requestData() {
        IExtractContract.View view = (IExtractContract.View) getView();
        if (null != view) {
            view.showLoading();
            YpFreshApi.newInstance().getSSService()
                    .getDeliveryCode()
                    .compose(((BaseFragment) getView()).<List<CodeBean>>applySchedulers(FragmentEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CodeBean>>() {
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
                        public void onNext(List<CodeBean> codeBeans) {
                            view.onResponseData(true, codeBeans);
                            view.hideLoading();
                        }
                    });
        }
    }
}
