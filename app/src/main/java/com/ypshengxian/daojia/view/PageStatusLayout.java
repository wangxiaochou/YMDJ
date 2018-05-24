package com.ypshengxian.daojia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ypshengxian.daojia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面状态布局
 *
 * @author Yan
 * @date 2017.06.15
 * @note 此控件为页面状态显示布局
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PageStatusLayout extends RelativeLayout {
    /** 隐藏 */
    public static final int STATUS_HIDE = -1;
    /** 正在加载 */
    public static final int STATUS_LOADING = 0;
    /** 加载失败 */
    public static final int STATUS_LOAD_FAIL = 1;
    /** 加载超时 */
    public static final int STATUS_TIMEOUT = 2;
    /** 空空如也 */
    public static final int STATUS_EMPTY = 3;
    /** 网络错误 */
    public static final int STATUS_NETWORK_ERROR = 4;
    /** 页面删除 */
    public static final int STATUS_DELETED = 5;
    /** 暂未开放 */
    public static final int STATUS_NOT_AVAILABLE = 6;

    /** 状态列表 */
    public List<View> mStatusLayoutList = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public PageStatusLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param attrs 属性
     */
    public PageStatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 索引条的三参构造函数
     *
     * @param context 上下文
     * @param attrs 属性
     */
    public PageStatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void init(Context context) {
        /*
         * 以下顺序必须与常量定义的顺序一致
         */
        LayoutInflater inflater = LayoutInflater.from(context);
        // 正在加载(动画)
        mStatusLayoutList.add(inflater.inflate(R.layout.page_status_loading, this, false));
        // 其他状态(静态图片)
        mStatusLayoutList.add(inflater.inflate(R.layout.page_status_layout, this, false));

        // 默认隐藏
        setVisibility(View.GONE);
    }

    /**
     * 显示状态
     *
     * @param status 状态码(参见STATUS_LOADING等)
     */
    public void showStatus(int status) {
        showStatus(status, null);
    }

    /**
     * 显示状态
     *
     * @param status 状态码(参见STATUS_LOADING等)
     * @param retryClickListener 重试点击监听(LOADING状态无效)
     */
    public void showStatus(int status, View.OnClickListener retryClickListener) {
        if (status < 0) {
            setVisibility(View.GONE);
        } else {
            int index = status == STATUS_LOADING ? 0 : 1;
            try {
                View view = mStatusLayoutList.get(index);
                // 清除当前view
                if (getChildAt(0) != null) {
                    removeViewAt(0);
                }
                // 添加状态view
                addView(view);
                // 显示
                setVisibility(VISIBLE);

                if (status == STATUS_LOAD_FAIL) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_load_fail);

                } else if (status == STATUS_TIMEOUT) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_timeout);

                } else if (status == STATUS_EMPTY) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_empty);

                } else if (status == STATUS_NETWORK_ERROR) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_network_error);

                } else if (status == STATUS_DELETED) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_deleted);

                } else if (status == STATUS_NOT_AVAILABLE) {
                    ((ImageView) view.findViewById(R.id.iv_page_status_icon)).setImageResource(R.drawable.icon_no_msg);
                    ((TextView) view.findViewById(R.id.tv_page_status_text)).setText(R.string.page_status_not_available);
                }

                // 设置点击监听
                TextView retry = (TextView) view.findViewById(R.id.tv_page_status_retry);
                if (retry != null) {
                    if (retryClickListener != null) {
                        retry.setOnClickListener(retryClickListener);
                        retry.setVisibility(View.VISIBLE);
                    } else {
                        retry.setVisibility(View.INVISIBLE);
                    }
                }

            } catch (Exception e) {
                // 不处理异常
            }
        }
    }
}
