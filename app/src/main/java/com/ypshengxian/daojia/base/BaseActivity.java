package com.ypshengxian.daojia.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.ypshengxian.daojia.annotation.ActivityOption;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.view.LoadingDialog;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Activity基类
 *
 * @author mos
 * @date 2017.01.23
 * @note 1. 项目中所有子类必须继承自此基类
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseActivity extends AppCompatActivity implements BaseView, LifecycleProvider<ActivityEvent> {
    /** RxJava生命周期管理 */
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    /** 是否需要退出 */
    private boolean mIsExit = false;

    /**
     * 启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param intentFlags intent标识
     */
    public static void startActivityEx(Context context, Class<?> activityClass, int intentFlags) {
        startActivityEx(context, activityClass, null, intentFlags);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param data 参数
     * @param intentFlags intent标识
     */
    public static void startActivityEx(Context context, Class<?> activityClass, Bundle data, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 带转场的启动Activity(扩展)
     *
     * @param context 上下文
     * @param activityClass Activity的class
     * @param data Intent携带的数据
     * @param transitionActivityOptions 转场参数
     * @param intentFlags intent标识
     */
    public static void startActivityTranslationEx(Context context, Class<?> activityClass, Bundle data, ActivityOptionsCompat transitionActivityOptions, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(context, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                context.startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                context.startActivity(intent);
            }
        }
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activity activity
     * @param activityClass Activity的class
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public static void startActivityForResultEx(AppCompatActivity activity, Class<?> activityClass,
                                                int requestCode, int intentFlags) {
        startActivityForResultEx(activity, activityClass, requestCode, null, intentFlags);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activity activity
     * @param activityClass Activity的class
     * @param data 参数
     * @param requestCode 请求码
     * @param intentFlags intent标识
     */
    public static void startActivityForResultEx(AppCompatActivity activity, Class<?> activityClass, int requestCode,
                                                Bundle data, int intentFlags) {
        if (!hookRequestLogin(activityClass, data)) {
            Intent intent = new Intent(activity, activityClass);
            intent.setFlags(intentFlags);
            if (data != null) {
                intent.putExtras(data);
            }
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 拦截请求登录的页面
     *
     * @param activityClass 页面的类
     * @param data 参数
     * @return true -- 拦截成功  false -- 没有拦截
     */
    public static boolean hookRequestLogin(Class<?> activityClass, Bundle data) {
        // 查看该activity是否需要登录
        ActivityOption option = activityClass.getAnnotation(ActivityOption.class);
        if (option != null && option.reqLogin()) {

            return !BaseApplication.getInstance().checkLoginBeforeAction(activityClass.getName(), data);
        }

        return false;
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
        LoadingDialog.getInstance().show(this);
    }

    @Override
    public void showLoading(LoadingDialog.Option option) {
        LoadingDialog.getInstance().show(this, option);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().close();
    }

    @Nonnull
    @Override
    public Observable<ActivityEvent> lifecycle() {

        return mLifecycleSubject.asObservable();
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull ActivityEvent event) {

        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @Nonnull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {

        return RxLifecycleAndroid.bindActivity(mLifecycleSubject);
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onPause() {
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onStop() {
        mLifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        getWindow().getDecorView().removeCallbacks(null);
        super.onDestroy();
    }

    /**
     * 使用调度器
     *
     * @param event 生命周期
     * @return 调度转换器
     * @note 1. 若event传入null，则不绑定到生命周期，但依然会subscribe -> io，
     * observe -> ui。
     */
    public <T> Observable.Transformer<T, T> applySchedulers(final ActivityEvent event) {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                // 若不绑定到View的生命周期，则直接子线程中处理 -> UI线程中回调
                if (event != null) {
                    return observable.compose(BaseActivity.this.<T>bindUntilEvent(event))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }

                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 获取Activity
     *
     * @return BaseActivity
     */
    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    /**
     * 获取Context
     *
     * @return Context
     */
    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 通过兼容取Color
     *
     * @param resId ColorRes
     * @return ColorInt
     */
    @ColorInt
    public int getCompatColor(@ColorRes int resId) {
        return ContextCompat.getColor(this, resId);
    }

    /**
     * 通过兼容器取Drawable
     *
     * @param resId DrawableRes
     * @return Drawable
     */
    public Drawable getCompatDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(this, resId);
    }

    /**
     * 通过根布局在主线程的Handle运行runnable
     *
     * @param runnable task
     */
    public void post(Runnable runnable) {
        getWindow().getDecorView().post(runnable);
    }

    /**
     * 通过根布局在主线程的Handle运行runnable,delay为延迟
     *
     * @param runnable task
     * @param delay 延迟,单位为ms
     */
    public void postDelayed(Runnable runnable, int delay) {
        getWindow().getDecorView().postDelayed(runnable, delay);
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
        startActivityEx(this, activityClass, data, 0);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param requestCode 请求码
     */
    public void startActivityForResultEx(Class<?> activityClass, int requestCode) {
        startActivityForResultEx(this, activityClass, requestCode, null, 0);
    }

    /**
     * 启动Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param requestCode 请求码
     * @param data 数据
     */
    public void startActivityForResultEx(Class<?> activityClass, int requestCode, Bundle data) {
        startActivityForResultEx(this, activityClass, requestCode, data, 0);
    }

    /**
     * 启动Activity并清空在其之上的Activity(扩展)
     *
     * @param activityClass Activity的class
     */
    public void startActivityClearTopEx(Class<?> activityClass) {
        startActivityClearTopEx(activityClass, null);
    }

    /**
     * 启动Activity并清空在其之上的Activity(扩展)
     *
     * @param activityClass Activity的class
     * @param data 数据
     */
    public void startActivityClearTopEx(Class<?> activityClass, Bundle data) {
        startActivityEx(this, activityClass, data, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 启动Activity并清空Activity栈(扩展)
     *
     * @param activityClass Activity的class
     */
    public void startActivityClearTaskEx(Class<?> activityClass) {
        startActivityClearTaskEx(activityClass, null);
    }

    /**
     * 启动Activity并清空Activity栈(扩展)
     *
     * @param activityClass Activity的class
     * @param data 数据
     */
    public void startActivityClearTaskEx(Class<?> activityClass, Bundle data) {
        startActivityEx(this, activityClass, data, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    /**
     * 启动Activity转场(扩展)
     * {@link BaseActivity#startActivityTranslationEx(Context, Class, Bundle, ActivityOptionsCompat, int)}
     */
    public void startActivityTranslationEx(Class<?> activityClass, ActivityOptionsCompat transitionActivityOptions) {
        startActivityTranslationEx(this, activityClass, null, transitionActivityOptions, 0);
    }

    /**
     * 启动Activity转场(扩展)
     * {@link BaseActivity#startActivityTranslationEx(Context, Class, Bundle, ActivityOptionsCompat, int)}
     */
    public void startActivityTranslationEx(Class<?> activityClass, Bundle bundle, ActivityOptionsCompat transitionActivityOptions) {
        startActivityTranslationEx(this, activityClass, bundle, transitionActivityOptions, 0);
    }

    /**
     * 显示Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected void showFragment(int containerId, Fragment fragment) {
        if (fragment == null) {

            return;
        }

        String tag = fragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment cache = fm.findFragmentByTag(fragment.getTag());
        if (cache != null) {
            ft.show(cache);
        } else {
            ft.add(containerId, fragment, tag);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换Fragment
     *
     * @param containerId 容器id
     * @param fragment fragment
     */
    protected void replaceFragment(int containerId, Fragment fragment) {
        if (fragment == null) {

            return;
        }

        String tag = fragment.getClass().getSimpleName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏fragment
     *
     * @param fragment fragment
     */
    protected void hideFragment(Fragment fragment) {
        if (fragment == null) {

            return;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment cache = fm.findFragmentByTag(fragment.getTag());
        if (cache != null) {
            ft.hide(cache);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = super.dispatchTouchEvent(ev);
        /*
         * 若点击Activity的任何区域(除了输入框之外，应隐藏键盘)
         */
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();

            if (v != null && shouldHideInput(v, ev)) {
                hideInput(v);
            }
        }

        return ret;
    }

    /**
     * 是否应该隐藏输入
     *
     * @param v 焦点控件
     * @param event 动作事件
     * @return true -- 是  false -- 否
     */
    protected boolean shouldHideInput(View v, MotionEvent event) {
        boolean should = true;

        // 仅点击到输入框时，键盘不隐藏
        if (v != null && v instanceof EditText) {
            int[] loc = new int[2];
            v.getLocationOnScreen(loc);

            // 焦点控件位置
            int left = loc[0];
            int top = loc[1];
            int right = left + v.getWidth();
            int bottom = top + v.getHeight();

            int touchX = (int) event.getRawX();
            int touchY = (int) event.getRawY();

            // 是否点击到输入框
            if ((touchX >= left && touchX <= right) &&
                    (touchY >= top && touchY <= bottom)) {

                should = false;
            }
        }

        return should;
    }

    /**
     * 隐藏键盘
     *
     * @param v 控件
     */
    private void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 检测登录的拦截器接口
     */
    public static interface ICheckLoginInterceptor {
        /**
         * 在活动之前检测登录情况
         *
         * @param actionAfterLogin 登录后的action
         * @param data 参数
         * @return true -- 已经登录 false -- 没有登录
         * @note 1. 实现者若检测到没有登录，则需要自己的处理未登录的逻辑
         * 2. 实现者若自己处理了未登录的逻辑，可执行actionAfterLogin实现隐式启动活动
         * 3. 若需要通过actionAfterLogin隐式启动活动，则需将该活动名作为action注册到
         * intent filter之中
         */
        public boolean checkLoginBeforeAction(String actionAfterLogin, Bundle data);
    }
    @Override
    public void onBackPressed() {

        setAppExit(0, "");
    }

    /**
     * app退出设置
     *
     * @param second 恢复秒数
     * @param message 信息
     */
    protected void setAppExit(int second, String message) {
        if (second == 0 && TextUtils.equals("", message)) {
            super.onBackPressed();
            return;
        }

        if (mIsExit) {
            super.onBackPressed();
        } else {
            mIsExit = true;
            showToast(message);
            Observable.timer(second, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            mIsExit = false;
                        }
                    });
        }
    }
}
