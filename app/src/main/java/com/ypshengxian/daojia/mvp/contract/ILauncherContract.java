package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;

/**
 * 欢迎界面
 *
 * @author Yan
 * @date 2018-04-03
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface ILauncherContract {
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
        void onResponseData(boolean isSuccess,Object data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         *
         *
         */
        void requestData();
    }
}
