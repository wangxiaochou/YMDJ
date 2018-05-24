package com.ypshengxian.daojia.task;

import android.app.Activity;

import com.ypshengxian.daojia.utils.ThreadUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity栈工具
 *
 * @author Yan
 * @date 2017.02.22
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ActivityStack {
    /** 活动栈 */
    private static List<WeakReference<Activity>> sActivityStack = new ArrayList<>();

    /**
     * 添加活动
     *
     * @param activity 活动对象
     */
    public static void addActivity(Activity activity) {
        if (activity != null) {
            WeakReference<Activity> ref = new WeakReference<Activity>(activity);
            sActivityStack.add(ref);
        }
    }

    /**
     * 结束掉所有活动
     */
    public static void finishActivities() {
        // 在UI线程中执行finish动作
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int size = sActivityStack.size();
                for (int i = size - 1; i >= 0; --i) {
                    WeakReference<Activity> ref = sActivityStack.get(i);
                    Activity activity = ref.get();
                    if (activity != null) {
                        activity.finish();
                    }
                    sActivityStack.remove(ref);
                }
            }
        });
    }
}
