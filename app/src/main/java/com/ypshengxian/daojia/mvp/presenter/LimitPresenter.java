package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ILimitContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.bean.FlashBean;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 限时
 *
 * @author Yan
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LimitPresenter extends BasePresenter<ILimitContract.View> implements ILimitContract.Presenter {
    @Override
    public void requestData() {
        ILimitContract.View view=getView();
        if(null!=view) {
            YpFreshApi.newInstance().getSSService()
                    .getFlashSales()
                    .compose(((BaseActivity) getView()).<List<FlashBean>>applySchedulers(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<FlashBean>>() {
                        @Override
                        public void onCompleted() {
                            view.hideLoading();
                            view.onResponseData(false, null);
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.hideLoading();
                            view.onResponseData(false, null);
                        }

                        @Override
                        public void onNext(List<FlashBean> flashBeans) {
                            view.hideLoading();
                            view.onResponseData(true, flashBeans);
                        }
                    });
        }

    }
}
