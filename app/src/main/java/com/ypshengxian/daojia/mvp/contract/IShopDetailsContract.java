package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.TopBean;

import java.util.Map;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-28
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface IShopDetailsContract {
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
        void onResponseData(boolean isSuccess, TopBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onGetNewSales(boolean isSuccess, TopBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onAddCart(boolean isSuccess, AddCartBean data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         *
         * @param id id
         */
        void requestData(String id);

        /**
         * 请求数据
         *
         * @param map 数据
         */
        void getNewSales(Map<String, String> map);

        /**
         * 添加购物车
         *
         * @param map 数据
         */
        void addCart(Map<String, String> map);

    }
}
