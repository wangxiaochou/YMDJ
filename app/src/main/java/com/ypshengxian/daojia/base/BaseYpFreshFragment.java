package com.ypshengxian.daojia.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.ypshengxian.daojia.utils.ViewUtils;
import com.ypshengxian.daojia.view.PageStatusLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-02-07
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public abstract class BaseYpFreshFragment extends BaseFragment {
    /** 根布局 */
    private View mView;
    /** 间隔订阅者 */
    public Subscription mIntervalSubscribe;
    /** 页面状态布局 */
    private PageStatusLayout mPageStatusLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(getContentView(), container, false);
        initView();

        // 页面状态布局
        mPageStatusLayout = new PageStatusLayout(getContext());
        ViewUtils.coverView(getView(), mPageStatusLayout, false);

        return mView;
    }

    /**
     * 获得根布局
     *
     * @return View 根布局
     */
    @Override
    public View getView() {
        return mView;
    }

    /**
     * 设置ContentView的ResId
     *
     * @return ContentView的ResId
     */
    public abstract int getContentView();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 查询控件
     *
     * @param id id
     * @return View
     */
    public View findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Override
    public void onStart() {
        super.onStart();

        interval(0, null);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mIntervalSubscribe != null) {
            mIntervalSubscribe.unsubscribe();
        }
    }

    /**
     * 轮循
     *
     * @param second 间隔时间
     * @param onIntervalCallListener 触发事件
     */
    protected void interval(long second, final OnIntervalCallListener onIntervalCallListener) {
        if (second == 0) {
            return;
        }

        mIntervalSubscribe = Observable
                .interval(second, TimeUnit.SECONDS)
                .compose(applySchedulers(FragmentEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (onIntervalCallListener != null) {
                            onIntervalCallListener.onIntervalCall();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // 不做处理
                    }
                });
    }
    /**
     * 正在加载
     */
    public void showStatusLoading() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_LOADING);
    }


    /**
     * 正在加载
     *
     * @param listener 点击监听
     */
    public void showStatusLoading(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_LOADING, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 隐藏
     */
    public void showStatusHide() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_HIDE);
    }

    /**
     * 隐藏
     *
     * @param listener 点击监听
     */
    public void showStatusHide(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_HIDE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 加载失败
     */
    public void showStatusLoadFail() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_LOAD_FAIL);
    }

    /**
     * 加载失败
     *
     * @param listener 点击监听
     */
    public void showStatusLoadFail(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_LOAD_FAIL, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 加载超时
     */
    public void showStatusTimeout() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_TIMEOUT);
    }

    /**
     * 加载超时
     *
     * @param listener 点击监听
     */
    public void showStatusTimeout(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_TIMEOUT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 空空如也
     */
    public void showStatusEmpty() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_EMPTY);
    }

    /**
     * 空空如也
     *
     * @param listener 点击监听
     */
    public void showStatusEmpty(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_EMPTY, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 网络错误
     */
    public void showStatusNetwordError() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_NETWORK_ERROR);
    }

    /**
     * 网络错误
     *
     * @param listener 点击监听
     */
    public void showStatusNetwordError(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_NETWORK_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 页面删除
     */
    public void showStatusDeleted() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_DELETED);
    }

    /**
     * 页面删除
     *
     * @param listener 点击监听
     */
    public void showStatusDeleted(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_DELETED, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 暂未开放
     */
    public void showStatusNotAvailable() {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_NOT_AVAILABLE);
    }

    /**
     * 暂未开放
     *
     * @param listener 点击监听
     */
    public void showStatusNotAvailable(final View.OnClickListener listener) {
        mPageStatusLayout.showStatus(PageStatusLayout.STATUS_NOT_AVAILABLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
    }

    /**
     * 当间隔时间到了回调的接口定义
     */
    public interface OnIntervalCallListener {
        /**
         * 间隔时间到后调用方法
         */
        void onIntervalCall();
    }


}
