package com.ypshengxian.daojia.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseApplication;
import com.ypshengxian.daojia.view.ToastCompat;


/**
 * <p> 吐司相关工具类 </p><br>
 *
 * @author lwc
 * @date 2017/3/10 16:00
 * @note -
 * init              : 吐司初始化
 * showShortToastSafe: 安全地显示短时吐司
 * showLongToastSafe : 安全地显示长时吐司
 * showShortToast    : 显示短时吐司
 * showLongToast     : 显示长时吐司
 * cancelToast       : 取消吐司显示
 * isNotificationEnabled：用来判断Toast是否开启通知权限
 * -------------------------------------------------------------------------------------------------
 * @ modified -
 * @ date -
 * @ note -
 */
public class ToastUtils {
    /** Toast默认位置 */
    private static final int GRAVITY_DETAIL = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    /** 实体 */
    private static Object sToast;
    /** 主线程handle */
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    /** 当连续弹出吐司时，是要弹出新吐司还是只修改文本内容 */
    private static boolean isJumpWhenMore;

    /**
     * 构造类
     */
    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 吐司初始化
     *
     * @param isJumpWhenMore 当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
     * <p>{@code true}: 弹出新吐司<br>{@code false}: 只修改文本内容</p>
     * <p>如果为{@code false}的话可用来做显示任意时长的吐司</p>
     */
    public static void init(boolean isJumpWhenMore) {
        ToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToastSafe(final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToastSafe(final CharSequence text, final int gravity) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_SHORT, gravity);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 文本资源id
     */
    public static void showShortToastSafe(final @StringRes int resId, final int gravity) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(Utils.getContext().getResources().getString(resId), Toast.LENGTH_SHORT, gravity);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortToastSafe(final @StringRes int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(resId, Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args 参数
     */
    public static void showShortToastSafe(final @StringRes int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(resId, Toast.LENGTH_SHORT, args);
            }
        });
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args 参数
     */
    public static void showShortToastSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(format, Toast.LENGTH_SHORT, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToastSafe(final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongToastSafe(final @StringRes int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(resId, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args 参数
     */
    public static void showLongToastSafe(final @StringRes int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(resId, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args 参数
     */
    public static void showLongToastSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(format, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShortToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args 参数
     */
    public static void showShortToast(@StringRes int resId, Object... args) {
        showToast(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args 参数
     */
    public static void showShortToast(String format, Object... args) {
        showToast(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToast(CharSequence text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongToast(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     * @param args 参数
     */
    public static void showLongToast(@StringRes int resId, Object... args) {
        showToast(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示长时吐司
     *
     * @param format 格式
     * @param args 参数
     */
    public static void showLongToast(String format, Object... args) {
        showToast(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示吐司
     *
     * @param resId 资源Id
     * @param duration 显示时长
     */
    private static void showToast(@StringRes int resId, int duration) {
        showToast(Utils.getContext().getResources().getText(resId).toString(), duration);
    }

    /**
     * 显示吐司
     *
     * @param resId 资源Id
     * @param duration 显示时长
     * @param args 参数
     */
    private static void showToast(@StringRes int resId, int duration, Object... args) {
        showToast(String.format(Utils.getContext().getResources().getString(resId), args), duration);
    }

    /**
     * 显示吐司
     *
     * @param format 格式
     * @param duration 显示时长
     * @param args 参数
     */
    private static void showToast(String format, int duration, Object... args) {
        showToast(String.format(format, args), duration);
    }

    /**
     * 显示吐司
     *
     * @param text 文本
     * @param duration 显示时长
     */
    private static void showToast(CharSequence text, int duration, int gravity) {
        Resources resources = Utils.getContext().getResources();
        int resourceId = resources.getIdentifier("toast_y_offset", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        showToast(text, duration, gravity, 0, height);
    }

    /**
     * 显示吐司
     *
     * @param text 文本
     * @param duration 显示时长
     */
    private static void showToast(CharSequence text, int duration) {
        Resources resources = Utils.getContext().getResources();
        int resourceId = resources.getIdentifier("toast_y_offset", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        showToast(text, duration, GRAVITY_DETAIL, 0, height);
    }

    /**
     * 显示吐司
     *
     * @param text 文本
     * @param duration 显示时长
     */
    private static void showToast(CharSequence text, int duration, int gravity, int xOffSet, int yOffSet) {
        // 若字符串为空，则不处理
        if (TextUtils.isEmpty(text)) {

            return;
        }

        View v = null;
        TextView tv = null;
        if (isJumpWhenMore) {
            cancelToast();
        }

        if (sToast == null) {
            LayoutInflater inflate;
            if (BaseApplication.getRefActivity() != null) {
                inflate = (LayoutInflater) BaseApplication.getRefActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            } else {
                inflate = (LayoutInflater) Utils.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            v = inflate.inflate(R.layout.layout_toast, null);
            if (isNotificationEnabled()) {
                sToast = new Toast(Utils.getContext());
                ((Toast) sToast).setView(v);
            } else {
                sToast = new ToastCompat(Utils.getContext());
                ((ToastCompat) sToast).setView(v);
            }
        } else {
            if (sToast instanceof Toast) {
                v = ((Toast) sToast).getView();
            }
            if (sToast instanceof ToastCompat) {
                v = ((ToastCompat) sToast).getView();
            }
        }

        if (v != null) {
            tv = (TextView) v.findViewById(R.id.message);
        }
        if (null != tv) {
            tv.setText(text);
        }
        if (sToast instanceof Toast) {
            ((Toast) sToast).setDuration(duration);
            ((Toast) sToast).setGravity(gravity, xOffSet, yOffSet);
            ((Toast) sToast).show();
        }
        if (sToast instanceof ToastCompat) {
            ((ToastCompat) sToast).setDuration(duration);
            ((ToastCompat) sToast).setGravity(gravity, xOffSet, yOffSet);
            ((ToastCompat) sToast).show();
        }
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            if (sToast instanceof Toast) {
                ((Toast) sToast).cancel();
            }
            if (sToast instanceof ToastCompat) {
                ((ToastCompat) sToast).cancel();
            }
            sToast = null;
        }
    }

    /**
     * 用来判断Toast是否开启通知权限
     *
     * @return true 开通权限 false 没有开通权限
     */
    private static boolean isNotificationEnabled() {
        return NotificationManagerCompat.from(Utils.getContext()).areNotificationsEnabled();
    }
}