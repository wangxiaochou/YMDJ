package com.ypshengxian.daojia.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ResUtils;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-16
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class EmptyView extends LinearLayout {

    private LayoutInflater mInflater;
    /** 上下文对象 */
    private Context mContext;
    /** 提示 消息 */
    private TextView mTvMessage;
    /** 去首页 */
    private Button mBtnGoHome;

    public EmptyView(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    /**
     * 初始化视图
     */
    private void initView() {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.layout_empty, this, true);
        mTvMessage = (TextView) rootView.findViewById(R.id.tv_layout_empty_text);
        mBtnGoHome = (Button) rootView.findViewById(R.id.btn_layout_empty_go_home);
        //默认隐藏
        mTvMessage.setVisibility(GONE);
        mBtnGoHome.setVisibility(GONE);

    }

    /**
     * 设置提示文本
     *
     * @param res 资源
     */
    public EmptyView setMessage(@StringRes int res) {
        String message = ResUtils.getString(res);
        return setMessage(message);
    }

    /**
     * 设置提示文本
     *
     * @param message 文本
     */
    public EmptyView setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return this;
        }
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(message);
        return this;
    }

    /**
     * 设置Btn的文本和点击事件
     *
     * @param res 文本
     * @param listener 监听
     */
    public EmptyView setBtnText(@StringRes int res, View.OnClickListener listener) {
        String text = ResUtils.getString(res);
        return setBtnText(text, listener);
    }

    /**
     * 设置Btn的文本和点击事件
     *
     * @param text 文本
     * @param listener 监听
     */
    public EmptyView setBtnText(String text, View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            return this;
        }
        mBtnGoHome.setVisibility(VISIBLE);
        mBtnGoHome.setText(text);
        mBtnGoHome.setOnClickListener(listener);

        return this;
    }

}
