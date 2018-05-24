package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.FlashBean;

import java.util.List;

/**
 * 限时
 *
 * @author Yan
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface ILimitContract {

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
        void onResponseData(boolean isSuccess,List<FlashBean> data);


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
