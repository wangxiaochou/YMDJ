package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.EventBean;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-27
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface IEventContract {
    /**
     * View的接口
	 */
    interface View extends BaseView {
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseData(boolean isSuccess, List<EventBean> data);

    }

    /**
     * Presenter的接口
     */
    interface Presenter {


        /**
         * 请求数据
         */
        void requestData();
    }
}
