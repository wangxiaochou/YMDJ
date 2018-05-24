package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;

import java.util.Map;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-29
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface ICartContract {
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
        void onResponseData(boolean isSuccess, AddCartBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onDeletedCart(boolean isSuccess, AddCartBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onAddCart(boolean isSuccess, AddCartBean data);
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onUpdateCart(boolean isSuccess, AddCartBean data);
        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onBatchUpdateCart(boolean isSuccess, AddCartBean data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 请求数据
         */
        void requestData();

        /**
         * 删除商品
         *
         * @param map 数据
         */
        void deletedCart(Map<String, String> map);

        /**
         * 添加商品
         *
         * @param map 数据
         */
        void addCart(Map<String, String> map);

        /**
         * 修改购物车选中状态
         *
         * @param map 数据
         */
        void updateCart(Map<String, String> map);

        /**
         * 是否全选
         *
         * @param map 书记
         *
         */
        void batchUpdateCart(Map<String, String> map);
    }

}
