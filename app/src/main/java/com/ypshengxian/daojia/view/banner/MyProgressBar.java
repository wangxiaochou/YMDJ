package com.ypshengxian.daojia.view.banner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.utils.ResUtils;
import com.ypshengxian.daojia.utils.Utils;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-20
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class MyProgressBar extends LinearLayout {
    /** 布局构建 */
    private LayoutInflater mLayoutInflater;
    /** 进度条 */
    private ProgressBar mNumberProgressBar;
    /** 已抢 */
    private TextView mPrice;
    /** 进度百分比 */
    private TextView mNum;

    /** 进度 */
    private int mProgress=0;

    /** 最大 */
    private int mMaxProgress=0;
    /** 监听 */
    private OnProgressChangeListener mProgressChangeListener;


    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /** 初始化View */
    private void initView() {
        mLayoutInflater = LayoutInflater.from(Utils.getContext());
        View view = mLayoutInflater.inflate(R.layout.progress_bar_layout, this,true);
        mNumberProgressBar= (ProgressBar) view.findViewById(R.id.pb_progress_bar);
        mPrice= (TextView) view.findViewById(R.id.tv_progress_piece);
        mNum= (TextView) view.findViewById(R.id.tv_progress_num);
    }

    /**
     * 设置监听器
     *
     * @param mProgressChangeListener 监听器
     */
    public void setmProgressChangeListener(OnProgressChangeListener mProgressChangeListener) {
        this.mProgressChangeListener = mProgressChangeListener;
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress){
        this.mProgress=progress;
        mNumberProgressBar.setProgress(progress);
        if(null!=mProgressChangeListener){
            mProgressChangeListener.onProgressChange(progress);
        }

    }

    /**
     * 设置最大进度
     *
     */
    public void  setMaxProgress(int progress){
        this.mMaxProgress=progress;
        mNumberProgressBar.setMax(progress);
    }

    /**
     * 设置数据百分比进度
     *
     */
    public void  setTextToProgressView(String text){
       mNum.setText(text+"%");
    }

    /**
     * 设置数据百分比进度
     *
     */
    public void  setTextToRobView(String text){
       mPrice.setText(String.format(ResUtils.getString(R.string.limit_rob),text));
    }

    /**
     * 监听进度变化
     *
     */
    interface  OnProgressChangeListener{
        /**
         * 当数据变化时
         * @param progress 进度
         */
        void onProgressChange(int progress);
    }
}
