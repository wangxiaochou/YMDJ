package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.PayBean;


/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IPayWayContract {
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
        void onResponseData(boolean isSuccess, PayBean data);

    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         */
        void requestData(String gateway, String orderSn, String type, String coinAmount, String payPassword);

    }
}
