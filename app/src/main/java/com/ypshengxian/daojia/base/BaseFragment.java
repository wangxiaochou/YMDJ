package com.ypshengxian.daojia.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.LoadingDialog;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * fragment基类
 *
 * @author mos
 * @date 2017.01.23
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @modified - lwc
 * @date - 2017.5.4
 * @note - loadData - 懒加载数据，使用如下：
 * 1. 重写loadData方法，将加载数据的逻辑放在方法内。通过firstLoad变量，判断loadData是否是第一次调用。
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseFragment extends Fragment implements BaseView, LifecycleProvider<FragmentEvent> {
    /** 生命周期管理 */
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    /** 绑定上下文 */
    private Context mContext;
    /** 布局是否初始化完成 */
    private boolean mIsViewCreated;
    /** 数据在进入的是否刷新 */
    private boolean mIsFirstLoadData = true;

    /**
     * 启动Activity(扩展)
     *
     * @param fragment fragment
     * @param activityClass Activity的class
     * @param data 参数
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public static void startActivityForResultEx(Fragment fragment, Class<?> activityClass, int requestCode,
                                                Bundle data, int intentFlags) {
        if (!BaseActivity.hookRequestLogin(activityClass, data) && fragment != null) {
            Intent intent = new Intent(fragment.getContext(), activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public String TAG() {

        return getClass().getSimpleName();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showShortToastSafe(msg);
    }

    @Override
    public void showLoading() {
        LoadingDialog.getInstance().show(getActivity());
    }

    /**
     * 为showDialog配置参数
     *
     * @param option 参数
     */
    @Override
    public void showLoading(LoadingDialog.Option option) {
        LoadingDialog.getInstance().show(getActivity(), option);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().close();
    }

    @Nonnull
    @Override
    public Observable<FragmentEvent> lifecycle() {

        return mLifecycleSubject.asObservable();
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull FragmentEvent event) {

        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {

        return RxLifecycleAndroid.bindFragment(mLifecycleSubject);
    }

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mLifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        // 布局创建成功
        mIsViewCreated = true;
        return rootView;
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(FragmentEvent.START);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(FragmentEvent.RESUME);

        if (!isHidden() && getUserVisibleHint()) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    @CallSuper
    @Override
    public void onPause() {
        mLifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    @Override
    public void onStop() {
        mLifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @CallSuper
    @Override
    public void onDetach() {
        mLifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!isHidden()) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mIsViewCreated) {
            // 加载数据
            loadData(mIsFirstLoadData);
            mIsFirstLoadData = false;
        }
    }

    /**
     * Fragment懒加载数据，主要为了解决ViewPage忽略Fragment生命周期的问题
     *
     * @param firstLoad 是否第一次加载数据
     */
    public void loadData(boolean firstLoad) {
    }

    /**
     * 获取Activity
     *
     * @return BaseActivity
     */
    @Override
    public BaseActivity getBaseActivity() {
        if (null == mContext) {
            throw new RuntimeException("This is an empty object");
        }
        if (mContext instanceof BaseActivity) {
            return (BaseActivity) mContext;
        } else {
            throw new RuntimeException("please let all Activity extends BaseActivity");
        }
    }

    /**
     * 获取Context
     *
     * @return Context
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 通过兼容取Color
     *
     * @param resId ColorRes
     * @return ColorInt
     */
    @ColorInt
    public int getCompatColor(@ColorRes int resId) {
        return ContextCompat.getColor(getActivity(), resId);
    }

    /**
     * 通过兼容器取Drawable
     *
     * @param resId DrawableRes
     * @return Drawable
     */
    public Drawable getCompatDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(getActivity(), resId);
    }

    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     * @note 1. 若event传入null，则不绑定到生命周期，但依然会subscribe -> io，
     * observe -> ui。
     */
    public <T> Observable.Transformer<T, T> applySchedulers(final FragmentEvent event) {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
                if (event != null) {
                    return observable.compose(BaseFragment.this.<T>bindUntilEvent(event))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }

                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param data 参数
     * @param intentFlags intent标识
     */
    public void startActivityEx(Class<?> activityClass, Bundle data, int intentFlags) {
        Context context = getContext();
        if (!BaseActivity.hookRequestLogin(activityClass, data) && context != null) {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            startActivity(intent);
        }
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param data 参数
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public void startActivityForResultEx(Class<?> activityClass, int requestCode,
                                         Bundle data, int intentFlags) {
        startActivityForResultEx(this, activityClass, requestCode, data, intentFlags);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     */
    public void startActivityEx(Class<?> activityClass) {
        startActivityEx(activityClass, null);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param data 数据
     */
    public void startActivityEx(Class<?> activityClass, Bundle data) {
        startActivityEx(activityClass, data, 0);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param requestCode 请求码
     */
    public void startActivityForResultEx(Class<?> activityClass, int requestCode) {
        startActivityForResultEx(activityClass, requestCode, null, 0);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param requestCode 请求码
     * @param data 数据
     */
    public void startActivityForResultEx(Class<?> activityClass, int requestCode, Bundle data) {
        startActivityForResultEx(activityClass, requestCode, data, 0);
    }
}
