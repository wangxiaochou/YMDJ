package com.ypshengxian.daojia.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ResUtils;

/**
 * 谊品风格dialog
 *
 * @author Yan
 * @date 2018-04-08
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class YpDialog extends PopupDialog {
    /** 标题 */
    private TextView mTvTitle;
    /** 内容 */
    private TextView mTvMessage;
    /** 水平方向分割线 */
    private View mIvDivisionHor;
    /** 取消按钮 */
    private Button mBtnCancel;
    /** 确认按钮 */
    private Button mBtnConfirm;
    /** 中间容器 */
    private ViewGroup mVgCenterGroup;
    /** 上部容器 */
    private ViewGroup mVgTopGroup;
    /** 中间占位View */
    private TextView  mTvCenter;

    /** 是否显示标题 */
    private boolean mIsShowTitle;
    /** 是否显示内容 */
    private boolean mIsShowMessage;
    /** 是否显示取消按钮 */
    private boolean mIsShowCancel;
    /** 是否显示确定按钮 */
    private boolean mIsShowConfirm;
    /** 是否显示中间容器 */
    private boolean mIsShowCenterGroup;
    /** 是否显示上部容器 */
    private boolean mIsShowTopGroup;
    /** 是否显示中间的View */
    private boolean mIsShowCenterView;

    /** 对话框对象 */
    private Dialog mDialog;
    /** 依附的界面 */
    private Activity mActivity;


    public YpDialog(Context context) {
        super(context, R.layout.yp_popup_dialog_view_alertdialog, R.style.FullScreenDialog);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mDialog = YpDialog.this;
        mActivity = (Activity) mContext;

        mWindow.setGravity(Gravity.CENTER);
        mWindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 获取布局中的控件
        mVgTopGroup = (ViewGroup) mRootView.findViewById(R.id.yp_fl_top);
        mTvTitle = (TextView) mRootView.findViewById(R.id.yp_tv_title);
        mVgCenterGroup = (ViewGroup) mRootView.findViewById(R.id.yp_fl_center);
        mTvMessage = (TextView) mRootView.findViewById(R.id.yp_tv_message);
        mBtnCancel = (Button) mRootView.findViewById(R.id.yp_btn_cancel);
        mBtnConfirm = (Button) mRootView.findViewById(R.id.yp_btn_confirm);
        mTvCenter=(TextView) findViewById(R.id.yp_tv_center);

        // 默认设置所有的控件隐藏
        mVgTopGroup.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);
        mVgCenterGroup.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.GONE);
        mBtnCancel.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
        mTvCenter.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return this
     */
    public YpDialog setTitle(String title) {
        mIsShowTitle = true;

        if (TextUtils.isEmpty(title)) {
            return this;
        }

        mTvTitle.setText(title);
        return this;
    }
    /**
     * 设置标题
     *
     * @param title 标题
     * @return this
     */
    public YpDialog setTitle(String title, @ColorRes int color, float textSize) {
        mIsShowTitle = true;

        if (TextUtils.isEmpty(title)) {
            return this;
        }

        mTvTitle.setText(title);
        mTvTitle.setTextSize(textSize);
        mTvTitle.setTextColor(ResUtils.getColor(color));
        return this;
    }


    /**
     * 设置内容
     *
     * @param msg 内容
     * @return this
     */
    public YpDialog setMessage(String msg) {
        mIsShowMessage = true;

        if (TextUtils.isEmpty(msg)) {
            return this;
        }

        mTvMessage.setText(msg);
        return this;
    }
    /**
     * 设置内容
     *
     * @param msg 内容
     * @return this
     */
    public YpDialog setMessage(String msg, @ColorRes int color, float textSize) {
        mIsShowMessage = true;

        if (TextUtils.isEmpty(msg)) {
            return this;
        }

        mTvMessage.setText(msg);
        mTvMessage.setTextSize(textSize);
        mTvMessage.setTextColor(ResUtils.getColor(color));
        return this;
    }

    /**
     * 设置内容
     *
     * @param messageId 内容
     * @return this
     */
    public YpDialog setMessage(@StringRes int messageId) {
        return setMessage(mActivity.getText(messageId).toString());
    }

    /**
     * 设置内容的Gravity
     *
     * @param gravity 对齐方式
     * @return this
     * @note 默认单行居中, 多行偏左
     * @see Gravity
     */
    public YpDialog setMessageGravity(int gravity) {
        mTvMessage.setGravity(gravity);

        return this;
    }

    /**
     * 设置上部的视图
     *
     * @param view 视图
     * @return this
     */
    public YpDialog setTopView(View view) {
        if (view == null) {
            return this;
        }

        mVgTopGroup.addView(view);

        mIsShowTopGroup = true;
        return this;
    }

    /**
     * 设置上部的视图
     *
     * @param layoutId 视图布局id
     * @return this
     */
    public YpDialog setTopView(@LayoutRes int layoutId) {
        if (mActivity == null) {
            return this;
        }

        return setTopView(mActivity.getLayoutInflater(), layoutId);
    }

    /**
     * 设置上部的视图
     *
     * @param inflater 填充器
     * @param layoutId 视图布局id
     * @return this
     */
    public YpDialog setTopView(LayoutInflater inflater, @LayoutRes int layoutId) {
        if (inflater == null) {
            return this;
        }

        View view = inflater.inflate(layoutId, mVgTopGroup, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        mVgTopGroup.addView(view, params);

        mIsShowTopGroup = true;
        return this;
    }

    /**
     * 设置中间的视图
     *
     * @param view 视图
     * @return this
     */
    public YpDialog setCenterView(View view) {
        if (view == null) {
            return this;
        }

        mVgCenterGroup.addView(view);

        mIsShowCenterGroup = true;
        return this;
    }

    /**
     * 设置中间的视图
     *
     * @param layoutId 视图布局id
     * @return this
     */
    public YpDialog setCenterView(@LayoutRes int layoutId) {
        if (mActivity == null) {
            return this;
        }

        return setCenterView(mActivity.getLayoutInflater(), layoutId);
    }

    /**
     * 设置中间的视图
     *
     * @param inflater 填充器
     * @param layoutId 视图布局id
     * @return this
     */
    public YpDialog setCenterView(LayoutInflater inflater, @LayoutRes int layoutId) {
        if (inflater == null) {
            return this;
        }

        View view = inflater.inflate(layoutId, mVgCenterGroup, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        mVgCenterGroup.addView(view, params);

        mIsShowCenterGroup = true;
        return this;
    }

    /**
     * 设置取消按钮
     *
     * @param textId 取消按钮上的文字
     * @param listener 取消的监听
     * @return this
     */
    public YpDialog setNegativeButton(@StringRes int textId, final View.OnClickListener listener) {
        return setNegativeButton(mActivity.getText(textId).toString(), true, listener);
    }

    /**
     * 设置取消按钮
     *
     * @param text 取消按钮上的文字
     * @param listener 取消的监听
     * @return this
     */
    public YpDialog setNegativeButton(String text, final View.OnClickListener listener) {
        return setNegativeButton(text, true, listener);
    }

    /**
     * 设置取消按钮
     *
     * @param text 取消按钮上的文字
     * @param isDismiss 是否点击后消失
     * @param listener 取消的监听
     * @return this
     */
    public YpDialog setNegativeButton(String text, final boolean isDismiss, final View.OnClickListener listener) {
        mIsShowCancel = true;
        mBtnCancel.setText(text);

        // 设置点击事件
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }

                if (isDismiss) {
                    dismiss();
                }
            }
        });
        return this;
    }

    /**
     * 设置确认按钮
     *
     * @param textId 确认按钮上的文字
     * @param listener 确认的监听
     * @return this
     */
    public YpDialog setPositiveButton(@StringRes int textId, final View.OnClickListener listener) {
        return setPositiveButton(mActivity.getText(textId).toString(), true, listener);
    }

    /**
     * 设置确认按钮
     *
     * @param text 确认按钮上的文字
     * @param listener 确认的监听
     * @return this
     */
    public YpDialog setPositiveButton(String text, final View.OnClickListener listener) {
        return setPositiveButton(text, true, listener);
    }

    /**
     * 设置确认按钮
     *
     * @param text 确认按钮上的文字
     * @param isDismiss 是否点击后消失
     * @param listener 确认的监听
     * @return this
     */
    public YpDialog setPositiveButton(String text, final boolean isDismiss, final View.OnClickListener listener) {
        mIsShowConfirm = true;
        mBtnConfirm.setText(text);

        // 设置点击事件
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }

                if (isDismiss) {
                    dismiss();
                }
            }
        });
        return this;
    }

    /**
     * 显示弹窗
     */
    @Override
    public void show() {
        setLayout();
        super.show();
    }

    /**
     * 设置布局的显示与隐藏
     */
    private void setLayout() {
        // 标题和内容都没有设置,显示提示标题
        if (!mIsShowTitle && !mIsShowMessage) {
            mTvTitle.setText("提示");
            mTvTitle.setVisibility(View.VISIBLE);
        }

        // 显示标题
        if (mIsShowTitle) {
            mVgTopGroup.setVisibility(View.VISIBLE);
            mTvTitle.setVisibility(View.VISIBLE);
        }

        // 显示上部容器
        if (mIsShowTopGroup) {
            mVgTopGroup.setVisibility(View.VISIBLE);
            mTvTitle.setVisibility(View.GONE);
        }

        // 显示内容
        if (mIsShowMessage) {
            mVgCenterGroup.setVisibility(View.VISIBLE);
            mTvMessage.setVisibility(View.VISIBLE);
        }

        // 显示中间容器
        if (mIsShowCenterGroup) {
            mVgCenterGroup.setVisibility(View.VISIBLE);
            mTvMessage.setVisibility(View.GONE);
        }

        // 确定和取消按钮都没有设置,不显示按钮,设置底部margin
        if (!mIsShowCancel && !mIsShowConfirm) {
            if (mIsShowMessage || mIsShowCenterGroup) {
                mVgCenterGroup.setPadding(0, 0, 0, mActivity.getResources().getDimensionPixelOffset(R.dimen.ios_popup_dialog_padding_no_button));
            } else {
                mVgTopGroup.setPadding(0, 0, 0, mActivity.getResources().getDimensionPixelOffset(R.dimen.ios_popup_dialog_padding_no_button));
            }
        }

        // 确定和取消按钮都设置了
        if (mIsShowConfirm && mIsShowCancel) {
            mTvCenter.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnConfirm.setBackgroundResource(R.drawable.shape_up_pop_right);
            mBtnCancel.setVisibility(View.VISIBLE);
            mBtnCancel.setBackgroundResource(R.drawable.shape_up_pop_left);
        }

        // 只设置了确定按钮
        if (mIsShowConfirm && !mIsShowCancel) {
            mTvCenter.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
            mBtnConfirm.setBackgroundResource(R.drawable.shape_up_pop_right);
        }

        // 只设置了取消按钮
        if (!mIsShowConfirm && mIsShowCancel) {
            mTvCenter.setVisibility(View.GONE);
            mBtnCancel.setVisibility(View.VISIBLE);
            mBtnCancel.setBackgroundResource(R.drawable.shape_up_pop_left);
        }
    }
}
