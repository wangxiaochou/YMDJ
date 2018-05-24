package com.ypshengxian.daojia.utils;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.TimerTask;

/**
 * <p>视图相关的Utils</p><br>
 *
 * @author lwc
 * @date 2017/3/14 18:18
 * @note -
 * findViewByXY :在View上的x,y值获得子View
 * getTouchTarget :通过xy在View上获得点击的目标view
 * isTouchPointInView :判断xy是否在view的大小内
 * setTextDrawable :给TextView设置Drawable,如果不设置，传0
 * -------------------------------------------------------------------------------------------------
 * @modified - lwc
 * @date - 2017/4/13 14.39
 * @note -
 * setImageViewSelector 通过图片的tint值设置图片的selector效果，Clickable=true才有效果
 * setViewSelector 通过代码写出shape,再通过代码设置selector，Clickable=true才有效果
 */
public class ViewUtils {
    /**
     * 构造类
     */
    private ViewUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 在View上的x,y值获得子View
     *
     * @param view 根View
     * @param x x值
     * @param y y值
     * @return child view
     */
    public static View findViewByXY(View view, int x, int y) {
        View targetView = null;
        if (view instanceof ViewGroup) {
            // 父容器,遍历子控件
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                targetView = findViewByXY(v.getChildAt(i), x, y);
                if (targetView != null) {
                    break;
                }
            }
        } else {
            targetView = getTouchTarget(view, x, y);
        }
        return targetView;

    }

    /**
     * 通过xy在View上获得点击的目标view
     *
     * @param view xy的View
     * @param x x值
     * @param y y值
     * @return view
     */
    public static View getTouchTarget(View view, int x, int y) {
        View targetView = null;
        // 判断view是否可以聚焦
        List<View> TouchableViews = view.getTouchables();
        for (View child : TouchableViews) {
            if (isTouchPointInView(child, x, y)) {
                targetView = child;
                break;
            }
        }
        return targetView;
    }

    /**
     * 使用布局替换子View
     *
     * @param rootView 根ViewGroup
     * @param childId 子View的id
     * @param layoutId 布局Id
     * @return 替换后的View
     */
    public static View replaceChildView(ViewGroup rootView, int childId, int layoutId) {
        if (rootView == null) {

            return null;
        }
        // 找到child
        View child = rootView.findViewById(childId);
        if (child == null) {

            return null;
        }
        // 得到child在parent中的位置
        ViewGroup parent = (ViewGroup) child.getParent();
        int index = parent.indexOfChild(child);
        // 移除child
        parent.removeView(child);
        // 添加布局到rootView
        child = LayoutInflater.from(Utils.getContext()).inflate(layoutId, rootView, false);
        parent.addView(child, index);

        return child;
    }

    /**
     * 使用coverView遮盖view
     *
     * @param view 被遮盖的view
     * @param coverView 遮盖的view
     * @param isReplace 是否替换被遮盖的view(保持view的相对层级关系)
     * @return 替换后的View
     */
    public static void coverView(final View view, final View coverView, final boolean isReplace) {
        if (null == view || null == coverView) {

            return;
        }
        view.post(new TimerTask() {
            @Override
            public void run() {
                if (view.getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) view.getParent();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    if (isReplace) {
                        int index = parent.indexOfChild(view);
                        parent.removeView(view);
                        FrameLayout child = new FrameLayout(Utils.getContext());
                        child.setLayoutParams(layoutParams);
                        child.addView(view);
                        child.addView(coverView);
                        parent.addView(child, index);
                    } else {
                        coverView.setLayoutParams(layoutParams);
                        ((ViewGroup) view.getParent()).addView(coverView);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            coverView.setElevation(1000);
                        }
                    }
                }
            }
        });
    }

    /**
     * 判断xy是否在view的大小内
     *
     * @param view 控件
     * @param x x值
     * @param y y值
     * @return 在View内返回true, 否则返回false
     */
    public static boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        return view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right;
    }

    /**
     * 给TextView设置Drawable,如果不设置，传0
     *
     * @param view TextView
     * @param drawbleLeftRes leftDrawable ResId
     * @param drawbleTopRes topDrawable ResId
     * @param drawbleRightRes rightDrawable ResId
     * @param drawbleBottomRes bottomDrawable ResId
     */
    public static void setTextDrawable(TextView view, @DrawableRes int drawbleLeftRes, @DrawableRes int drawbleTopRes, @DrawableRes int drawbleRightRes,
                                       @DrawableRes int drawbleBottomRes) {
        Drawable leftDrawable = null;
        Drawable topDrawable = null;
        Drawable rightDrawable = null;
        Drawable bottomDrawable = null;

        if (drawbleLeftRes != -1 && drawbleLeftRes != 0) {
            leftDrawable = ContextCompat.getDrawable(Utils.getContext(), drawbleLeftRes);
        }

        if (drawbleTopRes != -1 && drawbleTopRes != 0) {
            topDrawable = ContextCompat.getDrawable(Utils.getContext(), drawbleTopRes);
        }

        if (drawbleRightRes != -1 && drawbleRightRes != 0) {
            rightDrawable = ContextCompat.getDrawable(Utils.getContext(), drawbleRightRes);
        }

        if (drawbleBottomRes != -1 && drawbleBottomRes != 0) {
            bottomDrawable = ContextCompat.getDrawable(Utils.getContext(), drawbleBottomRes);
        }

        setTextDrawable(view, leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    /**
     * 给TextView设置Drawable,如果不设置，传null
     *
     * @param view TextView
     * @param leftDrawable leftDrawable
     * @param topDrawable topDrawable
     * @param rightDrawable rightDrawable
     * @param bottomDrawable bottomDrawable
     */
    public static void setTextDrawable(TextView view, Drawable leftDrawable, Drawable topDrawable, Drawable rightDrawable,
                                       Drawable bottomDrawable) {

        if (null != leftDrawable) {
            // 这一步必须要做,否则不会显示.
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        }
        if (null != topDrawable) {
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        }
        if (null != rightDrawable) {
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        }
        if (null != bottomDrawable) {
            bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(), bottomDrawable.getMinimumHeight());
        }
        view.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    /**
     * 获得屏幕密度
     *
     * @return density
     */
    public static float getDensity() {
        return Utils.getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 通过id查找view
     *
     * @param root 根view
     * @param id id
     * @return 查找到的view
     */
    public static <T extends View> T findViewById(View root, int id) {

        return (T) root.findViewById(id);
    }

    /**
     * 设置文本
     *
     * @param view 根view
     * @param resId TextView的资源id
     * @param text 字符串
     */
    public static void setText(View view, int resId, String text) {
        if (text == null || view == null) {

            return;
        }

        ((TextView) findViewById(view, resId)).setText(text);
    }

    /**
     * 设置可见性
     *
     * @param view 根view
     * @param resId 资源id
     * @param visibility 可见性
     */
    public static void setVisibility(View view, int resId, int visibility) {
        if (view == null) {

            return;
        }

        findViewById(view, resId).setVisibility(visibility);
    }

    /**
     * 设置点击事件
     *
     * @param view 根view
     * @param resId 资源id
     * @param listener 点击监听
     */
    public static void setOnClickListener(View view, int resId, View.OnClickListener listener) {
        if (view == null) {

            return;
        }

        findViewById(view, resId).setOnClickListener(listener);
    }


    /**
     * ImageView 添加 selector 颜色
     *
     * @param imageView imageView
     * @param drawableRes 背景图片
     * @param colorNormal 正常颜色
     * @param colorSelected 点击的颜色
     */
    public static void setImageViewSelector(ImageView imageView, @DrawableRes int drawableRes, @ColorInt int colorNormal, @ColorInt int colorSelected) {
        Drawable drawable = ContextCompat.getDrawable(Utils.getContext(), drawableRes);
        setImageViewSelector(imageView, drawable, colorNormal, colorSelected);
    }

    /**
     * ImageView 添加 selector 颜色
     *
     * @param imageView imageView
     * @param drawable 资源
     * @param colorNormal 正常颜色
     * @param colorSelected 点击的颜色
     */
    public static void setImageViewSelector(ImageView imageView, Drawable drawable, @ColorInt int colorNormal, @ColorInt int colorSelected) {
        int[] colors = new int[]{colorNormal, colorSelected};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        StateListDrawable stateListDrawable = new StateListDrawable();
        // 注意顺序
        stateListDrawable.addState(states[0], drawable);
        stateListDrawable.addState(states[1], drawable);
        Drawable.ConstantState state = stateListDrawable.getConstantState();
        drawable = DrawableCompat.wrap(state == null ? stateListDrawable : state.newDrawable()).mutate();
        DrawableCompat.setTintList(drawable, colorList);
        imageView.setImageDrawable(drawable);
    }

    /**
     * 通过代码写出 shape_normal 和 shape_press，
     * 再通过代码为一个{View} 设置 selector
     *
     * @param view 需要设置press_state 的控件
     * @param solidNormal normal状态下的solid
     * @param solidPress press状态下的solid
     * @param radius 拐角
     * @param strokeColor 边缘颜色
     * @param strokeWidth 边缘宽度
     */
    public static void setViewSelector(@NonNull View view, @ColorInt int solidNormal, @ColorInt int solidPress, int radius, @ColorInt int strokeColor, int strokeWidth) {
        // normal
        GradientDrawable gd_n = new GradientDrawable();
        gd_n.setColor(solidNormal);
        gd_n.setCornerRadius(radius);
        gd_n.setStroke(strokeWidth, strokeColor);

        // selected
        GradientDrawable gd_p = new GradientDrawable();
        gd_p.setColor(solidPress);
        gd_p.setCornerRadius(radius);
        gd_p.setStroke(strokeWidth, strokeColor);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, gd_p);
        drawable.addState(new int[]{-android.R.attr.state_pressed}, gd_n);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 扩大view的点击区域
     *
     * @param view view目标
     * @param expandTouchWidth 扩大区域
     */
    public static void setTouchDelegate(final View view, final int expandTouchWidth) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                // view构建完成后才能获取，所以放在post中执行
                // 4个方向增加矩形区域
                rect.top -= expandTouchWidth;
                rect.bottom += expandTouchWidth;
                rect.left -= expandTouchWidth;
                rect.right += expandTouchWidth;

                parentView.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }
}
