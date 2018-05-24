package com.ypshengxian.daojia.ui.activity;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.ILauncherContract;
import com.ypshengxian.daojia.mvp.presenter.LauncherPresenter;
import com.ypshengxian.daojia.preference.YPPreference;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 欢迎界面
 *
 * @author Yan
 * @date 2018-04-03
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LauncherActivity extends BaseMVPYpFreshActicity<ILauncherContract.View,LauncherPresenter> implements
ILauncherContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTimer();

    }

    @Override
    public void onResponseData(boolean isSuccess, Object data) {

    }

    @Override
    protected LauncherPresenter createPresenter() {
        return new LauncherPresenter();
    }

    /**
     * 计时器
     */
    private void initTimer() {
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onCompleted();
            }
        })
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        if(YPPreference.newInstance().getIsFirst()) {
                            startActivityEx(GuideActivity.class);
                            YPPreference.newInstance().setIsFirst(false);
                        }else {
                            startActivityEx(MainActivity.class);
                        }
                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_launcher;
    }
}
