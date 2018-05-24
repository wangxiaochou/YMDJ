package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.CreateOrderBean;
import com.ypshengxian.daojia.network.bean.PayOrderBean;

import java.util.Map;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface IPayOrderContract {
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
        void onResponseData(boolean isSuccess, PayOrderBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onCreateOrder(boolean isSuccess, CreateOrderBean data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         *
         * @param map id
         */
        void requestData(Map<String, String> map);

        /**
         * 提交订单
         *
         * @param shopId   商铺Id
         * @param couponId 优惠卷ID
         * @param packetId 购物袋ID
         */
        void createOrder(String shopId, String couponId, String packetId, String deliveryTime);
    }

}
