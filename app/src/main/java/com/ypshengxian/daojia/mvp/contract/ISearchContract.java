package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface ISearchContract {
    /**
     * View的接口
     */
    interface View extends BaseView {
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onResponseData(boolean isSuccess, Object data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         *
         * @param text 请求数据
         */
        void requestData(String text);

    }
}
