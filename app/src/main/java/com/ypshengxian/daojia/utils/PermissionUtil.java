package com.ypshengxian.daojia.utils;

import android.Manifest;
import android.support.v4.app.ActivityCompat;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.ypshengxian.daojia.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 权限工具
 *
 * @author mos
 * @date 2017.06.21
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PermissionUtil {
    /** 实例 */
    private static final PermissionUtil sOurInstance = new PermissionUtil();

    /**
     * 私有化构造函数
     */
    private PermissionUtil() {
    }

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static PermissionUtil getInstance() {
        return sOurInstance;
    }

    /**
     * 初始化权限
     *
     * @param activity 页面
     */
    public void initPermissions(BaseActivity activity) {
        if (activity == null) {

            return;
        }

        // 请求权限
        new RxPermissions(activity)
                .request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 请求权限
     *
     * @param activity Activity
     * @param permission 权限
     */
    public void requestPermission(final BaseActivity activity, final String permission) {
        requestPermission(activity, permission, null);
    }

    /**
     * 请求权限
     *
     * @param activity Activity
     * @param permission 权限
     * @param action 权限允许的操作
     */
    public void requestPermission(final BaseActivity activity, final String permission, final Action1<Boolean> action) {
        if (activity == null) {

            return;
        }
        final RxPermissions permissions = new RxPermissions(activity);
        Observable.just(permission)
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String s) {
                        if (permissions.isGranted(s)) {
                            //允许了，就直接返回true
                            return Observable.just(true);
                        }
                        return permissions.request(s);
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (null != action) {
                            action.call(aBoolean);
                        }
                        if (!aBoolean) {
                            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable.getMessage());
                        if (null != action) {
                            action.call(false);
                        }
                    }
                });
    }

    /**
     * 用RxJava的方式请求权限
     *
     * @param activity Activity
     * @param permission 权限
     * @return 观察者
     */
    public Observable<Boolean> requestPermissionWithRxJava(final BaseActivity activity, final String permission) {
        if (activity == null) {

            return null;
        }
        final RxPermissions permissions = new RxPermissions(activity);
        return Observable.just(permission)
                .flatMap(new Func1<String, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String s) {
                        if (permissions.isGranted(s)) {
                            //允许了，就直接返回true
                            return Observable.just(true);
                        }
                        return permissions.request(s);
                    }
                });
    }

    /**
     * 请求权限组
     *
     * @param activity Activity
     * @param permission 权限组
     */
    public void requestPermissions(final BaseActivity activity, String... permission) {
        requestPermissions(activity, null, permission);
    }


    /**
     * 请求权限组
     *
     * @param activity Activity
     * @param action 权限的操作
     * @param permission 权限组
     */
    public void requestPermissions(final BaseActivity activity, final Action1<Boolean> action, String... permission) {
        if (activity == null) {

            return;
        }
        final RxPermissions permissions = new RxPermissions(activity);
        Observable.just(permission)
                .map(new Func1<String[], String[]>() {
                    @Override
                    public String[] call(String[] strings) {
                        List<String> requests = new ArrayList<>();
                        for (String string : strings) {
                            if (!permissions.isGranted(string)) {
                                requests.add(string);
                            }
                        }
                        return requests.toArray(new String[]{});
                    }
                })
                .flatMap(new Func1<String[], Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String[] objects) {
                        if (0 == objects.length) {
                            //全部都允许了，就直接返回true
                            Observable.just(true);
                        }
                        return permissions.request(objects);
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (null != action) {
                            action.call(aBoolean);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.e(throwable.getMessage());
                        if (null != action) {
                            action.call(false);
                        }
                    }
                });
    }

    /**
     * 用RxJava的方式 请求权限组
     *
     * @param activity Activity
     * @param permission 权限组
     * @return 观察者
     */
    public Observable<Boolean> requestPermissionsWithRxJava(final BaseActivity activity, String... permission) {
        if (activity == null) {

            return null;
        }
        final RxPermissions permissions = new RxPermissions(activity);
        return Observable.just(permission)
                .map(new Func1<String[], String[]>() {
                    @Override
                    public String[] call(String[] strings) {
                        List<String> requests = new ArrayList<>();
                        for (String string : strings) {
                            if (!permissions.isGranted(string)) {
                                requests.add(string);
                            }
                        }
                        return requests.toArray(new String[]{});
                    }
                })
                .flatMap(new Func1<String[], Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(String[] objects) {
                        if (0 == objects.length) {
                            //全部都允许了，就直接返回true
                            Observable.just(true);
                        }
                        return permissions.request(objects);
                    }
                });
    }
}
