package com.ypshengxian.daojia.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ypshengxian.daojia.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <p>Toast 适配类</p><br>
 *
 * @author - lwc
 * @date - 2017/4/1 18:45
 * @note - 防止系统通知权限被拒绝，无法显示Toast
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ToastCompat {
    /** 短的持续时间 */
    private static final long mDurationShort = 2000;
    /** 长的持续时间 */
    private static final long mDurationLong = 3500;

    /** 队列消息 */
    private static final int MSG_ENQUEUE_TOAST = 0x01;
    /** 取消消息 */
    private static final int MSG_CANCEL_TOAST = 0x02;
    /** 下一个消息 */
    private static final int MSG_NEXT_TOAST = 0x03;
    /** 处理的 Handle */
    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        /** 消息队列 */
        Queue<ToastCompat> mTQ = new LinkedList<>();
        /** 当前Toast */
        ToastCompat mCurrentToast;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ENQUEUE_TOAST:
                    mTQ.add(((ToastCompat) msg.obj));
                    if (mCurrentToast == null) {
                        sendEmptyMessage(MSG_NEXT_TOAST);
                    }
                    break;
                case MSG_CANCEL_TOAST:
                    mTQ.remove(((ToastCompat) msg.obj));
                    if (mCurrentToast == msg.obj) {
                        removeMessages(MSG_NEXT_TOAST);
                        sendEmptyMessage(MSG_NEXT_TOAST);
                    }
                    break;
                case MSG_NEXT_TOAST:
                    if (mCurrentToast != null) {
                        mCurrentToast.mTN.handleHide();
                    }
                    mCurrentToast = mTQ.poll();
                    if (mCurrentToast != null) {
                        mCurrentToast.mTN.handleShow();
                        sendEmptyMessageDelayed(MSG_NEXT_TOAST, mCurrentToast.mDuration == Toast.LENGTH_LONG ? mDurationLong : mDurationShort);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /** 上下文 */
    private final Context mContext;
    /** 消息处理 */
    private final TN mTN;
    /** 持续时间 */
    private int mDuration;
    /** 布局 */
    private View mNextView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     * or {@link android.app.Activity} object.
     */
    public ToastCompat(Context context) {
        mContext = context;
        mTN = new TN();
        mTN.mY = context.getResources().getDimensionPixelSize(Resources.getSystem().getIdentifier("toast_y_offset", "dimen", "android"));
        mTN.mGravity = compatGetToastDefaultGravity(context);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     * or {@link android.app.Activity} object.
     * @param text The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link @Toast.LENGTH_SHORT} or
     * {@link @Toast.LENGTH_LONG}
     */
    public static ToastCompat makeText(Context context, CharSequence text, @Duration int duration) {
        ToastCompat result = new ToastCompat(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(Resources.getSystem().getIdentifier("transient_notification", "layout", "android"), null);
        TextView tv = (TextView) v.findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        tv.setText(text);

        result.mNextView = v;
        result.mDuration = duration;

        return result;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     * or {@link android.app.Activity} object.
     * @param resId The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link @Toast.LENGTH_SHORT} or
     * {@link @Toast.LENGTH_LONG}
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static ToastCompat makeText(Context context, @StringRes int resId, @Duration int duration) throws Resources.NotFoundException {
        return makeText(context, context.getString(resId), duration);
    }

    private int compatGetToastDefaultGravity(Context context) {
        int toastDefaultGravityId = Resources.getSystem().getIdentifier("config_toastDefaultGravity", "integer", "android");
        if (toastDefaultGravityId != 0) {
            return context.getResources().getInteger(toastDefaultGravityId);
        } else {
            return Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        }
    }

    /**
     * Show the view for the specified duration.
     */
    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        TN tn = mTN;
        tn.mNextView = mNextView;
        Message.obtain(mHandler, MSG_ENQUEUE_TOAST, this).sendToTarget();
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public void cancel() {
        Message.obtain(mHandler, MSG_CANCEL_TOAST, this).sendToTarget();
    }

    /**
     * Return the view.
     *
     * @see #setView
     */
    public View getView() {
        return mNextView;
    }

    /**
     * Set the view to show.
     *
     * @see #getView
     */
    public void setView(View view) {
        mNextView = view;
    }

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    @Duration
    public int getDuration() {
        return mDuration;
    }

    /**
     * Set how long to show the view for.
     *
     * @see Toast
     */
    public void setDuration(@Duration int duration) {
        mDuration = duration;
    }

    /**
     * Set the margins of the view.
     *
     * @param horizontalMargin The horizontal margin, in percentage of the
     * container width, between the container's edges and the
     * notification
     * @param verticalMargin The vertical margin, in percentage of the
     * container height, between the container's edges and the
     * notification
     */
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mTN.mHorizontalMargin = horizontalMargin;
        mTN.mVerticalMargin = verticalMargin;
    }

    /**
     * Return the horizontal margin.
     */
    public float getHorizontalMargin() {
        return mTN.mHorizontalMargin;
    }

    /**
     * Return the vertical margin.
     */
    public float getVerticalMargin() {
        return mTN.mVerticalMargin;
    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @see Gravity
     * @see #getGravity
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mTN.mGravity = gravity;
        mTN.mX = xOffset;
        mTN.mY = yOffset;
    }

    /**
     * Get the location at which the notification should appear on the screen.
     *
     * @see Gravity
     * @see #getGravity
     */
    public int getGravity() {
        return mTN.mGravity;
    }

    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    public int getXOffset() {
        return mTN.mX;
    }

    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    public int getYOffset() {
        return mTN.mY;
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param s The new text for the Toast.
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(Resources.getSystem().getIdentifier("message", "id", "android"));
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param resId The new text for the Toast.
     */
    public void setText(@StringRes int resId) {
        setText(mContext.getText(resId));
    }

    /**
     * Gets the LayoutParams for the Toast window.
     *
     * @hide
     */
    public WindowManager.LayoutParams getWindowParams() {
        return mTN.mParams;
    }

    /**
     * 持续时间
     */
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    @interface Duration {
    }

    /**
     * <p>消息处理</p><br>
     *
     * @author - lwc
     * @date - 2017/4/1 18:52
     * @note -
     * -------------------------------------------------------------------------------------------------
     * @modified -
     * @date -
     * @note -
     */
    private static class TN {
        /** WindowManager 参数 */
        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        /** 位置 */
        int mGravity;
        /** 偏移 */
        int mX, mY;
        /** 水平间隔 */
        float mHorizontalMargin;
        /** 垂直间隔 */
        float mVerticalMargin;
        /** 布局 */
        View mView;
        /** 设置的布局 */
        View mNextView;
        /** WindowManager */
        WindowManager mWM;

        /**
         * 构造类
         */
        TN() {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = R.style.ToastCompat_Animation;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        /**
         * 处理显示
         */
        public void handleShow() {
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                Context context = mView.getContext();
                String packageName = mView.getContext().getPackageName();
                if (context == null) {
                    context = mView.getContext();
                }
                mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                // We can resolve the Gravity here by using the Locale for getting
                // the layout direction
                final int gravity = GravityCompat.getAbsoluteGravity(mGravity, ViewCompat.getLayoutDirection(mView));
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                mParams.packageName = packageName;
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
                    }
                    mWM.addView(mView, mParams);
                    trySendAccessibilityEvent();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }

        /**
         * 发送可访问事件
         */
        private void trySendAccessibilityEvent() {
            AccessibilityManager accessibilityManager =
                    (AccessibilityManager) mView.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
            if (!accessibilityManager.isEnabled()) {
                return;
            }
            // treat toasts as notifications since they are used to
            // announce a transient piece of information to the user
            AccessibilityEvent event = AccessibilityEvent.obtain(
                    AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
            event.setClassName(getClass().getName());
            event.setPackageName(mView.getContext().getPackageName());
            mView.dispatchPopulateAccessibilityEvent(event);
            accessibilityManager.sendAccessibilityEvent(event);
        }

        /**
         * 处理隐藏
         */
        public void handleHide() {
            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }

                mView = null;
            }
        }
    }


}
