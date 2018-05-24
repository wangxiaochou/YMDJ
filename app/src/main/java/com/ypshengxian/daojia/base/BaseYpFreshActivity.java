package com.ypshengxian.daojia.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.ypshengxian.daojia.utils.ViewUtils;
import com.ypshengxian.daojia.view.PageStatusLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 基础
 *
 * @author Yan
 * @date 2018-02-07
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public abstract class BaseYpFreshActivity extends BaseActivity {
    /** 意图 */
    public Intent mIntent;
    /** 间隔订阅者 */
    public Subscription mIntervalSubscribe;
    /** 是否在onCreate生命周期 */
    public boolean mIsInOnCreate;
    /** 是否第一次加载数据 */
    private boolean mIsFirstLoad = true;
    /** 跟布局 */
    protected View mContentView;
    /** 页面状态布局 */
    private PageStatusLayout mPageStatusLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContentView = View.inflate(this, getContentView(), null);
        setContentView(mContentView);
        mIsInOnCreate = true;

        initModuledata();
        initModuleView();

    }

    /**
     * 跟布局ID
     *
     * @return ContentView的ResId
     */
    public abstract int getContentView();

    /**
     * 初始化布局
     */
    private void initModuleView() {
        // 页面状态布局
        mPageStatusLayout = new PageStatusLayout(this);
        ViewUtils.coverView(mContentView, mPageStatusLayout, false);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mIsInOnCreate = false;

        // 加载数据
        if (mIsFirstLoad) {
            loadDataOnce();
            mIsFirstLoad = false;
        }
        loadDataAlways();

        interval(0, null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mIsInOnCreate = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsInOnCreate = false;
    }


    @Override
    protected void onPause() {
        super.onPause();

        mIsInOnCreate = false;
    }


    @Override
    protected void onStop() {
        super.onStop();

        mIsInOnCreate = false;

        if (mIntervalSubscribe != null) {
            mIntervalSubscribe.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIsInOnCreate = false;
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
                .compose(applySchedulers(ActivityEvent.DESTROY))
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
     * 当间隔时间到了回调的接口定义
     */
    public interface OnIntervalCallListener {
        /**
         * 间隔时间到后调用方法
         */
        void onIntervalCall();
    }

    /**
     * 获取下拉刷新布局
     *
     * @return 布局id
     */
    public int getRefreshView() {
        return 0;
    }


    /**
     * 总是重新加载数据
     */
    public void loadDataAlways() {
        // 不处理
    }

    /**
     * 只加载数据一次
     */
    public void loadDataOnce() {
        // 不处理
    }

    /**
     * 初始化数据
     */
    private void initModuledata() {
        mIntent = getIntent();
    }

    /**
     * 获取意图里面的String
     *
     * @param key key
     * @return value
     */
    public String getStringExtra(String key) {
        return mIntent.hasExtra(key) ? mIntent.getStringExtra(key) : "";
    }

    /**
     * 获取意图里面的Boolean
     *
     * @param key key
     * @return value
     */
    public boolean getBooleanExtra(String key) {
        return mIntent.hasExtra(key) ? mIntent.getBooleanExtra(key, false) : false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 响应登录成功
     *
     * @param isSuccess 是否成功
     * @note 子类刷新页面
     */
    public void onAcitivityResultFromLogin(boolean isSuccess) {

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



}
