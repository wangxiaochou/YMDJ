package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.UserCoinBean;
import com.ypshengxian.daojia.network.bean.UserPointFlowBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IIntegralDetailContract {

    interface View extends BaseView {
        /**
         * 返回数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseData(boolean isSuccess, UserPointFlowBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onGetUserCoin(boolean isSuccess, UserCoinBean data);
    }

    interface Presenter {
        /**
         * 请求数据
         *
         * @param map 数据
         */
        void requestData(Map<String, String> map);

        /**
         * 请求数据
         */
        void getUserCoin();
    }
}
