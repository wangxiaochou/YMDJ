package com.ypshengxian.daojia.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseApplication;
import com.ypshengxian.daojia.utils.BarUtils;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;

import java.lang.ref.WeakReference;

/**
 * 简单的标题栏构造器
 *
 * @author mos
 * @date 2017.02.23
 * @note 1. 使用此构造器之前，必须在Activity的layout中include simple_title_bar.xml。
 * 2. 请在setContentView之后，调用attach函数。
 * 3. 请在Application初始化时，调用initDefaultOptions，设置标题栏各项参数。
 * -------------------------------------------------------------------------------------------------
 * @modified mos
 * @date 2017.04.18
 * @note 1. attach函数增加对ViewGroup的支持，并非一定要传递activity。
 * 2. 若不传递activity，则不会使用沉浸式状态栏。
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class SimpleTitleBarBuilder implements View.OnClickListener {
    /** 默认参数 */
    private static DefaultOptions sOptions;
    /** 左边文本 */
    private TextView mTvLeftText;
    /** 右边文本 */
    private TextView mTvRightText;
    /** 标题文本 */
    private TextView mTvTitle;
    /** 左边容器 */
    private ViewGroup mVgLeftGroup;
    /** 右边容器 */
    private ViewGroup mVgRightGroup;
    /** 中间容器 */
    private ViewGroup mVgCenterGroup;
    /** 背景 */
    private ViewGroup mVgBackground;
    /** 阴影 */
    private View mVShadow;
    /** 标题点击监听 */
    private View.OnClickListener mTitleClickListener;
    /** 左边容器点击监听 */
    private View.OnClickListener mLeftClickListener;
    /** 右边容器点击监听 */
    private View.OnClickListener mRightClickListener;
    /** 页面的引用 */
    private WeakReference<Activity> mActivityRef;
    /** DrawerLayoutId */
    private int mDrawerLayoutId;
    /** 标题栏的背景颜色 */
    private int mBackGroundColor;
    /** 标题栏的背景alpha */
    private int mBackgroundAlpha;

    /**
     * 私有化构造函数
     */
    private SimpleTitleBarBuilder() {
    }

    /**
     * 初始化默认参数
     *
     * @param options 参数值
     */
    public static void initDefaultOptions(DefaultOptions options) {
        sOptions = options;
    }

    /**
     * 获取默认参数
     *
     * @return 默认参数对象
     */
    public static DefaultOptions getDefaultOptions() {

        return sOptions;
    }

    /**
     * 设置默认参数
     *
     * @param builder SimpleTitleBar构造器
     */
    private static void setDefaultOptions(SimpleTitleBarBuilder builder) {
        // 背景
        ViewGroup.LayoutParams params = builder.mVgBackground.getLayoutParams();
        params.height = sOptions.titleBarHeight;
        builder.mVgBackground.setLayoutParams(params);
        builder.setBackgroundColor(sOptions.backgroundColor);

        // 左边
        builder.setLeftTextColor(sOptions.leftTextColor);
        builder.setLeftTextSize(sOptions.leftTextSize);

        // 右边
        builder.setRightTextColor(sOptions.rightTextColor);
        builder.setRightTextSize(sOptions.rightTextSize);

        // 标题
        builder.setTitleColor(sOptions.titleColor);
        builder.setTitleSize(sOptions.titleSize);

        // 阴影
        builder.setShadowHeight(sOptions.shadowHeight);
        builder.setShadowColor(sOptions.shadowColor);
    }

    /**
     * 关联页面
     *
     * @param activity 页面
     * @return SimpleTitleBar构造器
     */
    public static SimpleTitleBarBuilder attach(Activity activity) {

        return attach(activity, null, R.id.rellay_simple_title_bar_background, 0);
    }

    /**
     * 关联页面
     *
     * @param activity 页面
     * @param containerView 容器View
     * @return SimpleTitleBar构造器
     */
    public static SimpleTitleBarBuilder attach(Activity activity, View containerView) {

        return attach(activity, containerView, R.id.rellay_simple_title_bar_background, 0);
    }

    /**
     * 关联页面
     *
     * @param containerView 容器View
     * @return SimpleTitleBar构造器
     * @note 不会设置沉浸式状态栏
     */
    public static SimpleTitleBarBuilder attach(View containerView) {

        return attach(null, containerView, R.id.rellay_simple_title_bar_background, 0);
    }

    /**
     * 关联页面
     *
     * @param activity 页面
     * @param hasIncludeId 是否在include时，创建了id
     * @param includeId include时的id
     * @return SimpleTitleBar构造器
     */
    public static SimpleTitleBarBuilder attach(Activity activity, boolean hasIncludeId, int includeId) {
        if (hasIncludeId) {

            return attach(activity, null, includeId, 0);
        }

        return attach(activity, null, R.id.rellay_simple_title_bar_background, 0);
    }

    /**
     * 关联页面
     *
     * @param activity 页面
     * @param drawerLayoutId DrawerLayout的资源id
     * @return SimpleTitleBar构造器
     * @note 若根布局为DrawerLayout，需传入其id，用于设置沉浸式状态栏
     */
    public static SimpleTitleBarBuilder attach(Activity activity, int drawerLayoutId) {

        return attach(activity, null, R.id.rellay_simple_title_bar_background, drawerLayoutId);
    }

    /**
     * 关联到布局
     *
     * @param activity 页面
     * @param containerView 容器
     * @param rootLayoutId 根布局id
     * @param drawerLayoutId DrawerLayout的资源id
     * @return SimpleTitleBar构造器
     * @note 1. 若根布局为DrawerLayout，需传入其id，用于设置沉浸式状态栏。
     * 2. 若containerView不为空，则布局从containerView之中加载。
     * 3. 若activity为空，则无法设置沉浸式状态栏。
     */
    public static SimpleTitleBarBuilder attach(Activity activity, View containerView, int rootLayoutId, int drawerLayoutId) {
        if (activity == null && containerView == null) {

            return null;
        }

        // 若activity为空，则取containerView
        if (containerView == null) {
            containerView = activity.getWindow().getDecorView();
        }

        SimpleTitleBarBuilder builder = new SimpleTitleBarBuilder();
        try {
            builder.mTvLeftText = (TextView) containerView.findViewById(R.id.tv_simple_title_bar_left_text);
            checkNotNullOrThrow(builder.mTvLeftText);
            builder.mTvRightText = (TextView) containerView.findViewById(R.id.tv_simple_title_bar_right_text);
            checkNotNullOrThrow(builder.mTvRightText);
            builder.mTvTitle = (TextView) containerView.findViewById(R.id.tv_simple_title_bar_title);
            checkNotNullOrThrow(builder.mTvTitle);
            builder.mVgLeftGroup = (ViewGroup) containerView.findViewById(R.id.fralay_simple_title_bar_left);
            checkNotNullOrThrow(builder.mVgLeftGroup);
            builder.mVgRightGroup = (ViewGroup) containerView.findViewById(R.id.fralay_simple_title_bar_right);
            checkNotNullOrThrow(builder.mVgRightGroup);
            builder.mVgCenterGroup = (ViewGroup) containerView.findViewById(R.id.fralay_simple_title_bar_center);
            checkNotNullOrThrow(builder.mVgCenterGroup);
            builder.mVgBackground = (ViewGroup) containerView.findViewById(rootLayoutId);
            checkNotNullOrThrow(builder.mVgBackground);
            builder.mVShadow = containerView.findViewById(R.id.v_simple_title_bar_shadow);
            checkNotNullOrThrow(builder.mVShadow);

            builder.mActivityRef = new WeakReference<>(activity);
            builder.mDrawerLayoutId = drawerLayoutId;

            // 设置点击监听
            builder.mVgLeftGroup.setOnClickListener(builder);
            builder.mVgRightGroup.setOnClickListener(builder);
            builder.mTvTitle.setOnClickListener(builder);
        } catch (Exception e) {
            throw new RuntimeException("failed to attach to SimpleTitleBarBuilder, you may forget" +
                    " to include simple_title_bar.xml");
        }
        // 设置默认参数
        if (sOptions == null) {
            generateDefaultOptions(activity);
        }
        setDefaultOptions(builder);

        // 保存背景颜色
        builder.mBackGroundColor = sOptions.backgroundColor;
        builder.mBackgroundAlpha = 0;

        // 设置状态栏颜色
        if (sOptions.immersive) {
            setStatusBarColor(builder, builder.mBackGroundColor, builder.mBackgroundAlpha);
        }

        return builder;
    }

    /**
     * 设置状态栏颜色
     *
     * @param builder 标题栏构建器
     * @param color 颜色
     * @param alpha 透明度
     */
    private static void setStatusBarColor(SimpleTitleBarBuilder builder, int color, int alpha) {
        Activity activity = builder.mActivityRef.get();

        // 只有attach到activity上时，才使用沉浸式状态栏
        if (activity != null && sOptions.immersive) {
            // 使用沉浸式状态栏
            if (builder.mDrawerLayoutId != 0) {
                View v = activity.findViewById(builder.mDrawerLayoutId);
                if (v instanceof DrawerLayout) {
                    StatusBarUtil.setColorForDrawerLayout(activity, (DrawerLayout) v, color, alpha);
                }
            } else {
                StatusBarUtil.setColor(activity, color, alpha);
            }
        }
    }

    /**
     * 检查不为空，否则跑出异常
     *
     * @param object 对象
     */
    private static void checkNotNullOrThrow(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
    }

    /**
     * 生成默认参数
     *
     * @param context 上下文
     */
    private static void generateDefaultOptions(Context context) {
        sOptions = new DefaultOptions();

        // 背景颜色(只有通过Activity的context来获取)
        if (context != null) {
            sOptions.backgroundColor = getPrimaryColor(context);
        } else {
            sOptions.backgroundColor = ContextCompat.getColor(BaseApplication.getInstance(), R.color.simple_title_bar_def_bg);
        }

        // 返回按钮
        sOptions.leftBackDrawable = R.drawable.icon_shop_ge_back;

        context = BaseApplication.getInstance();
        Resources res = context.getResources();

        // 文字颜色
        int defTextColor = ContextCompat.getColor(context, R.color.simple_title_bar_def_text);
        sOptions.titleColor = defTextColor;
        sOptions.leftTextColor = defTextColor;
        sOptions.rightTextColor = defTextColor;

        // 高度
        sOptions.titleBarHeight = res.getDimensionPixelSize(R.dimen.simple_title_bar_default_height);

        // 文本大小
        sOptions.titleSize = px2sp(context, res.getDimensionPixelSize(R.dimen.font_huge));
        sOptions.leftTextSize = px2sp(context, res.getDimensionPixelSize(R.dimen.font_small));
        sOptions.rightTextSize = px2sp(context, res.getDimensionPixelSize(R.dimen.font_small));

        // 阴影
        sOptions.shadowHeight = (int) ResUtils.getDimensionPixelSize(R.dimen.simple_title_bar_shadow_height);
        sOptions.shadowColor = ResUtils.getColor(R.color.simple_title_bar_def_shadow);
    }

    /**
     * px转sp
     *
     * @param px px值
     * @return sp值
     */
    private static int px2sp(Context context, float px) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 获取主色
     *
     * @param context 上下文
     * @return 主色
     */
    private static int getPrimaryColor(final Context context) {
        int color = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
        if (color != 0) {
            TypedValue t = new TypedValue();
            context.getTheme().resolveAttribute(color, t, true);
            color = t.data;

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedArray t = context.obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.simple_title_bar_def_bg));
            t.recycle();

        } else {
            TypedArray t = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.simple_title_bar_def_bg));
            t.recycle();
        }

        return color;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.fralay_simple_title_bar_left == id) {
            // 点击左边
            if (mLeftClickListener != null) {
                mLeftClickListener.onClick(v);
            }

        } else if (R.id.fralay_simple_title_bar_right == id) {
            // 点击右边
            if (mRightClickListener != null) {
                mRightClickListener.onClick(v);
            }
        } else if (R.id.tv_simple_title_bar_title == id) {
            // 点击标题
            if (mTitleClickListener != null) {
                mTitleClickListener.onClick(v);
            }
        }
    }

    /**
     * 设置整个标题栏可见性
     *
     * @param visible 可见性
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleBarVisible(boolean visible) {
        mVgBackground.setVisibility(visible == true ? View.VISIBLE : View.GONE);

        return this;
    }

    /**
     * 设置左边点击为返回事件
     *
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftClickAsBack() {
        mLeftClickListener = new BackClickListener();
        sOptions.leftBackDrawable = sOptions.leftBackDrawable == 0 ? R.drawable.icon_shop_ge_back : sOptions.leftBackDrawable;
        setLeftDrawable(ResUtils.getDrawable(sOptions.leftBackDrawable));
        mTvLeftText.setVisibility(View.VISIBLE);

        return this;
    }

    /**
     * 设置左边点击监听
     *
     * @param listener 监听器
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftClickListener(View.OnClickListener listener) {
        mLeftClickListener = listener;

        return this;
    }

    /**
     * 设置右边点击监听
     *
     * @param listener 监听器
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightClickListener(View.OnClickListener listener) {
        mRightClickListener = listener;

        return this;
    }

    /**
     * 设置标题点击监听
     *
     * @param listener 监听器
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleClickListener(View.OnClickListener listener) {
        mTitleClickListener = listener;

        return this;
    }

    /**
     * 设置左边文本
     *
     * @param text 文本
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftText(String text) {
        if (text != null) {
            mTvLeftText.setText(text);
            setLeftVisible(true);
        }

        return this;
    }

    /**
     * 设置左边的图标
     *
     * @param drawable 图标
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvLeftText.setCompoundDrawables(drawable, null, null, null);
            setLeftVisible(true);
        }

        return this;
    }

    /**
     * 设置左边的图标
     *
     * @param drawableId 图标id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftDrawable(int drawableId) {
        setLeftDrawable(ResUtils.getDrawable(drawableId));

        return this;
    }

    /**
     * 设置左边可见性
     *
     * @param visible 可见性
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftVisible(boolean visible) {
        mTvLeftText.setVisibility(visible == true ? View.VISIBLE : View.GONE);

        return this;
    }

    /**
     * 设置左边文本颜色
     *
     * @param color 文本颜色
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftTextColor(int color) {
        mTvLeftText.setTextColor(color);

        return this;
    }

    /**
     * 填充左边的视图
     *
     * @param layoutId 视图布局id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder inflateLeftView(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(Utils.getContext());
        View view = inflater.inflate(layoutId, mVgLeftGroup, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        mVgLeftGroup.addView(view, params);

        // 隐藏左边控件
        setLeftVisible(false);

        return this;
    }

    /**
     * 设置左边文本大小
     *
     * @param textSize 文本大小(sp)
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setLeftTextSize(float textSize) {
        mTvLeftText.setTextSize(textSize);

        return this;
    }

    /**
     * 设置标题文本
     *
     * @param text 文本
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleText(String text) {
        if (text != null) {
            mTvTitle.setText(text);
        }

        return this;
    }

    /**
     * 设置标题文本
     *
     * @param text 文本
     * @param textLen 文本长度,防止网页标题过长
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleText(String text, @IntRange int textLen){
        if(null!=text&&textLen>0){
            mTvTitle.setEllipsize(TextUtils.TruncateAt.END);
            mTvTitle.setMaxEms(textLen);
            mTvTitle.setText(text);
        }
        return this;
    }

    /**
     * 设置标题文本颜色
     *
     * @param color 文本颜色
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleColor(int color) {
        mTvTitle.setTextColor(color);

        return this;
    }

    /**
     * 设置标题文本大小
     *
     * @param textSize 文本大小(sp)
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleSize(float textSize) {
        mTvTitle.setTextSize(textSize);

        return this;
    }

    /**
     * 设置右边文本
     *
     * @param text 文本
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightText(String text) {
        if (text != null) {
            mTvRightText.setText(text);
            setRightVisible(true);
        }

        return this;
    }

    /**
     * 设置右边的图标
     *
     * @param drawable 图标
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightDrawable(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvRightText.setCompoundDrawables(null, null, drawable, null);
            setLeftVisible(true);
        }

        return this;
    }

    /**
     * 设置右边可见性
     *
     * @param visible 可见性
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightVisible(boolean visible) {
        mTvRightText.setVisibility(visible == true ? View.VISIBLE : View.GONE);

        return this;
    }

    /**
     * 设置右边文本颜色
     *
     * @param color 文本颜色
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightTextColor(int color) {
        mTvRightText.setTextColor(color);

        return this;
    }

    /**
     * 填充右边的视图
     *
     * @param layoutId 视图布局id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder inflateRightView(int layoutId) {
        inflateRightView(layoutId, Utils.getContext());

        return this;
    }

    /**
     * 填充右边的视图
     *
     * @param layoutId 视图布局id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder inflateRightView(int layoutId, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, mVgRightGroup, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        mVgRightGroup.addView(view, params);

        // 隐藏右边控件
        setRightVisible(false);

        return this;
    }

    /**
     * 设置右边文本大小
     *
     * @param textSize 文本大小(sp)
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setRightTextSize(float textSize) {
        mTvRightText.setTextSize(textSize);

        return this;
    }

    /**
     * 填充中间的视图
     *
     * @param layoutId 视图布局id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder inflateCenterView(int layoutId) {
        Activity activity = mActivityRef.get();
        if (activity == null) {

            return this;
        }

        return inflateCenterView(activity.getLayoutInflater(), layoutId);
    }

    /**
     * 填充中间的视图
     *
     * @param inflater 填充器
     * @param layoutId 视图布局id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder inflateCenterView(LayoutInflater inflater, int layoutId) {
        if (inflater == null) {

            return this;
        }

        View view = inflater.inflate(layoutId, mVgCenterGroup, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        mVgCenterGroup.addView(view, params);

        // 隐藏中间控件
        setTitleVisible(false);

        return this;
    }

    /**
     * 设置标题可见性
     *
     * @param visible 可见性
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setTitleVisible(boolean visible) {
        mTvTitle.setVisibility(visible == true ? View.VISIBLE : View.GONE);

        return this;
    }


    /**
     * 获取背景色
     *
     * @return 背景色
     */
    public int getBackgroundColor() {
        if (sOptions == null) {

            return 0;
        }

        return sOptions.backgroundColor;
    }

    /**
     * 设置背景颜色
     *
     * @param color 颜色值
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setBackgroundColor(int color) {
        mBackGroundColor = color;
        mVgBackground.setBackgroundColor(mBackGroundColor);
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resId 资源id
     * @return SimpleTitleBar构造器
     */
    public SimpleTitleBarBuilder setBackgroundResource(int resId) {
        mVgBackground.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置背景透明度
     *
     * @param alpha 透明度值
     * @return this
     */
    public SimpleTitleBarBuilder setBackgroundAlpha(int alpha) {
        mBackgroundAlpha = alpha;
        mVgBackground.getBackground().setAlpha(mBackgroundAlpha);
        return this;
    }

    /**
     * 设置状态栏的颜色和Alpha
     *
     * @param color 颜色
     * @param alpha 透明度值
     * @return this
     */
    public SimpleTitleBarBuilder setStatusBarColorAlpha(int color, int alpha) {
        setStatusBarColor(this, color, alpha);
        return this;
    }

    /**
     * 设置状态栏的颜色和Alpha
     *
     * @param color 颜色
     * @param alpha 透明度值
     * @param lightStatusBar 是否为浅色状态栏
     * @return this
     */
    public SimpleTitleBarBuilder setStatusBarColorAlpha(int color, int alpha, boolean lightStatusBar) {
        setStatusBarColorAlpha(color, alpha);
        useLightStatusBar(lightStatusBar);
        return this;
    }

    /**
     * 设置阴影高度
     *
     * @param height 高度(px)
     * @return this
     */
    public SimpleTitleBarBuilder setShadowHeight(int height) {
        if (height <= 0) {
            mVShadow.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams = mVShadow.getLayoutParams();
            layoutParams.height = height;
            mVShadow.setLayoutParams(layoutParams);
        }

        return this;
    }

    /**
     * 设置阴影颜色
     *
     * @param color 颜色
     * @return this
     */
    public SimpleTitleBarBuilder setShadowColor(int color) {
        mVShadow.setBackgroundColor(sOptions.shadowColor);

        return this;
    }

    /**
     * 使用浅色状态栏(状态栏字体变深色)
     *
     * @param lightStatusBar 是否是浅色状态栏
     * @return this
     * @note 6.0+系统可以在浅色状态栏时，设置文本为深色
     */
    public SimpleTitleBarBuilder useLightStatusBar(boolean lightStatusBar) {
        BarUtils.useLightStatusBar(mActivityRef.get(), lightStatusBar);
        return this;
    }

    /**
     * 查找View
     *
     * @param id view的id
     * @return view对象
     */
    public <T extends View> T findViewById(int id) {

        return (T) mVgBackground.findViewById(id);
    }

    /**
     * 参数
     */
    public static class DefaultOptions {
        /** 背景颜色 */
        public int backgroundColor;
        /** 标题栏高度(px) */
        public int titleBarHeight;
        /** 左边文字颜色 */
        public int leftTextColor;
        /** 左边文字大小(sp) */
        public float leftTextSize;
        /** 默认左边返回图标资源id */
        public int leftBackDrawable;
        /** 右边文字颜色 */
        public int rightTextColor;
        /** 右边文字大小(sp) */
        public float rightTextSize;
        /** 标题文字颜色 */
        public int titleColor;
        /** 标题文字大小(sp) */
        public float titleSize;
        /** 阴影高度 */
        public int shadowHeight;
        /** 阴影颜色 */
        public int shadowColor;
        /** 是否使用沉浸式状态栏 */
        public boolean immersive = true;
    }

    /**
     * 返回点击监听
     */
    private class BackClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Activity activity = SimpleTitleBarBuilder.this.mActivityRef.get();
            if (activity != null) {
                activity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                activity.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        }
    }
}
