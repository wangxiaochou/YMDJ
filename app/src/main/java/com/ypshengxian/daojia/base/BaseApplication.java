package com.ypshengxian.daojia.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.ypshengxian.daojia.network.RetrofitWrapper;
import com.ypshengxian.daojia.utils.CrashUtils;
import com.ypshengxian.daojia.utils.ToastUtils;
import com.ypshengxian.daojia.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;




/**
 * Application基类
 *
 * @author Yan
 * @date 2017.01.23
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    /** 调试TAG前缀 */
    private static final String TAG_PREFIX = "CQMC";
    /** 单例 */
    private static BaseApplication sInstance = null;
    /** 静态对象引用(解决static变量被回收的问题) */
    private static ArrayList<Object> sStaticRefs = new ArrayList<>();
    /** 获取目前正在展示的Activity */
    private static WeakReference<Activity> sRefActivity;
    /** 登录检测拦截 */
    private BaseActivity.ICheckLoginInterceptor mCheckLoginInterceptor = null;

    /**
     * 单例模式
     *
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Nullable
    public static Activity getRefActivity() {
        return sRefActivity.get();
    }

    @CallSuper
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // 工具初始化
        Utils.init(this);

        // 工具类初始化
        ToastUtils.init(false);

        // 崩溃记录
        CrashUtils.getInstance().init();

        // Retrofit初始化
        RetrofitWrapper.init(this);

        //注册生命周期的回调
        registerActivityLifecycleCallbacks(this);
    }

    /**
     * 在下一步活动之前，检测登录状态
     *
     * @param actionAfterLogin 登录后，活动的action
     * @param data 参数
     * @return false -- 未登录  true -- 已登录
     */
    public boolean checkLoginBeforeAction(String actionAfterLogin, Bundle data) {
        if (mCheckLoginInterceptor != null) {

            return mCheckLoginInterceptor.checkLoginBeforeAction(actionAfterLogin, data);
        }

        return false;
    }

    /**
     * 设置登录检测拦截器
     *
     * @param interceptor 拦截器
     */
    public void setCheckLoginInterceptor(BaseActivity.ICheckLoginInterceptor interceptor) {
        mCheckLoginInterceptor = interceptor;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        sRefActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    /**
     * 添加静态强引用
     *
     * @param obj 引用对象
     * @note 为了解决部分重要数据被内存回收，故需将其生命周期绑定到application中。需谨慎使用，否则
     * 会造成内存泄露
     */
    public void addStaticRefs(Object obj) {
        sStaticRefs.add(obj);
    }
}
