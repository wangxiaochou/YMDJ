package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.LineItemBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IGetGoodsOrderinfoContract {

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
        void onResponseData(boolean isSuccess, LineItemBean data);
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
        void requestData(Map<String, String> map);
    }
}
