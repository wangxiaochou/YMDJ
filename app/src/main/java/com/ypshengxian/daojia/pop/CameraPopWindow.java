package com.ypshengxian.daojia.pop;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.FloatRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ResUtils;


/**
 * 页面
 *
 * @author Yan
 * @date 2018-02-08
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CameraPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity mContext;
    private View mView;
    private Button mBtnCamera;
    private Button mBtnPhoto;
    private Button mBtnCancle;
    private OnButtonClickListener mButtonClickListener;

    public CameraPopWindow(Activity context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.pop_camera, null);
        mBtnPhoto = mView.findViewById(R.id.btn_pop_camera_photo);
        mBtnCancle = mView.findViewById(R.id.btn_pop_camera_cancel);
        mBtnPhoto.setOnClickListener(this);
        mBtnCancle.setOnClickListener(this);
        this.setContentView(mView);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.pop_shop_anim);
        this.setBackgroundDrawable(new ColorDrawable(ResUtils.getColor(R.color.color_transparent)));
        black(0.5f);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                black(1f);
            }
        });
    }

    /**
     * 屏幕变暗
     *
     * @param v 灰度
     */
    private void black(@FloatRange(from = 0, to = 1) float v) {
        WindowManager.LayoutParams params = mContext.getWindow().getAttributes();
        params.alpha = v;
        mContext.getWindow().setAttributes(params);
    }

    public void setButtonClickListener(OnButtonClickListener mButtonClickListener) {
        this.mButtonClickListener = mButtonClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_pop_camera_photo:
                if (null != mButtonClickListener) {
                    mButtonClickListener.onImageClick();
                }
                break;
            case R.id.btn_pop_camera_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnButtonClickListener {
        void onImageClick();
    }
}
