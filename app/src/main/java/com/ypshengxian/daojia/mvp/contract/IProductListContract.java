package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;

import java.util.Map;

/**
 * 商品列表
 *
 * @author Yan
 * @date 2018-03-30
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface IProductListContract  {
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
        void onResponseData(boolean isSuccess, AllGoodsBean data);


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
         * @param map  请求的数据
         */
        void requestData(Map<String,String> map);

        /**
         * 添加购物车
         *
         * @param map 数据
         */
        void addCart(Map<String, String> map);

    }
}
