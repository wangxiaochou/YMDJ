package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.AllGoodsBean;
import com.ypshengxian.daojia.network.bean.CateGoryBean;

import java.util.List;
import java.util.Map;

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


public interface ICategoryContract {

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
        void onResponseData(boolean isSuccess, List<CateGoryBean> data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data 数据
         */
        void onGetGoods(boolean isSuccess, AllGoodsBean data);

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
         */
        void requestData();

        /**
         * 获得商品数据
         *
         * @param map 数据
         */
        void getGoods(Map<String, String> map);

        /**
         * 添加购物车
         *
         * @param map 数据
         */
        void addCart(Map<String, String> map);
    }
}
