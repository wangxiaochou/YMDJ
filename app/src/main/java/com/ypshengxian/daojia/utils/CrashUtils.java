package com.ypshengxian.daojia.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> 崩溃相关工具类 </p><br>
 *
 * @author lwc
 * @date 2017/3/10 15:37
 * @note -
 * getInstance: 获取单例
 * init       : 初始化
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CrashUtils implements UncaughtExceptionHandler {
    /** 单例 */
    private volatile static CrashUtils sInstance;
    private static ExecutorService sExecutor;
    /** 拦截器 */
    private UncaughtExceptionHandler mHandler;
    /** 初始化 */
    private boolean isInitialized;
    /** 错误文件放置位置 */
    private String crashDir;
    /** 版本名 */
    private String versionName;
    /** 版本号 */
    private int versionCode;

    /**
     * 构造类
     */
    private CrashUtils() {
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code CrashUtils.getInstance().init(this);}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>}</p>
     *
     * @return 单例
     */
    public static CrashUtils getInstance() {
        if (sInstance == null) {
            synchronized (CrashUtils.class) {
                if (sInstance == null) {
                    sInstance = new CrashUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    private static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    private static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 初始化崩溃的文件目录
     * 根据手机储存卡的情况，设置目录，release放在Apk的缓存目录，debug放在储存卡根目录
     *
     * @return 存放崩溃的文件目录
     */
    public static String getCrashFilePath() {
        File baseCache;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            boolean isDebug = Utils.getContext().getApplicationInfo() != null && (Utils.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (isDebug) {
                baseCache = Environment.getExternalStorageDirectory();
            } else {
                baseCache = Utils.getContext().getExternalCacheDir();
            }
        } else {
            baseCache = Utils.getContext().getCacheDir();
        }
        if (baseCache == null) {
            return null;
        }
        return baseCache.getPath() + File.separator + "cqCityCrash" + File.separator;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (isInitialized) {
            return true;
        }
        crashDir = getCrashFilePath();

        if (TextUtils.isEmpty(crashDir)) {
            return false;
        }
        try {
            PackageInfo pi = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return isInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        LogUtils.e(throwable);
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + now + ".txt";
        if (!createOrExistsFile(fullPath)) {
            return;
        }
        if (sExecutor == null) {
            sExecutor = Executors.newSingleThreadExecutor();
        }
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (pw != null) {
                        pw.close();
                    }
                }
            }
        });
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\nat Device Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nat Device Model       : " + Build.MODEL +// 设备型号
                "\nat Device CPU_ABI    : " + Build.CPU_ABI +// 主cpu,方法过时,但新方法要api21
                "\nat Device CPU_ABI2    : " + Build.CPU_ABI2 +// 副cpu,方法过时,但新方法要api21
                "\nat Android Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nat Android SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nat App VersionName    : " + versionName +
                "\nat App VersionCode    : " + versionCode +
                "\nat Crash Date    : " + TimeUtils.getNowTimeString() +
                "\nat APK IsDebug    : " + AppUtils.isAppDebug() +
                "\n************* Crash Log Head ****************\n\n";
    }
}
