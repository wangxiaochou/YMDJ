package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.MyOrderBean;

import java.util.Map;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface IOrderAllContract {
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
        void onGetMyOrder(boolean isSuccess, MyOrderBean data);
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onCloseOrder(boolean isSuccess, BaseModuleApiResult data);
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onRefundOrder(boolean isSuccess, BaseModuleApiResult data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         *
         * @param map 数据
         */
        void getMyOrder(Map<String, String> map);

        /**
         * 请求数据
         *
         * @param map 数据
         */
        void closeOrder(Map<String, String> map);


        /**
         * 退款或申请售后
         * @param orderSn 订单
         * @param refundAmount 金额
         * @param reason 原因
         * @param id id
         *
         */
        void refundOrder( String orderSn, String refundAmount,String reason, String[] id);

    }
}
