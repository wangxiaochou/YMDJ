package com.ypshengxian.daojia.mvp.presenter;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.base.BaseActivity;
import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IEventContract;
import com.ypshengxian.daojia.network.YpFreshApi;
import com.ypshengxian.daojia.network.bean.EventBean;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-27
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class EventPresenter extends BasePresenter<IEventContract.View> implements IEventContract.Presenter {
    @Override
    public void requestData() {
        IEventContract.View view=getView();
        if(null!=view) {
            view.showLoading();
            YpFreshApi.newInstance().getSSService()
                    .getTopPic()
                    .compose(((BaseActivity) getView()).<List<EventBean>>applySchedulers(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<EventBean>>() {
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
                        public void onNext(List<EventBean> eventBeans) {
                            view.hideLoading();
                            view.onResponseData(true, eventBeans);
                        }
                    });
        }
    }
}
