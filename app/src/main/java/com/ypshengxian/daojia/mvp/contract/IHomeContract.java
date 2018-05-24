package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.HomeBean;
import com.ypshengxian.daojia.network.bean.ShopBean;

import java.util.List;
import java.util.Map;

/**
 * 首页
 *
 * @author Yan
 * @date 2018-03-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface IHomeContract  {
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
        void onResponseData(boolean isSuccess,HomeBean data);
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
        void onGetShop(boolean isSuccess,List<ShopBean> data);


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

        /**
         * 添加购物车
         *
         * @param map 数据
         */
        void addCart(Map<String, String> map);

        /**
         * 店铺
         *
         */
        void getShop();

    }
}
