package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.CodeBean;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-05
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public interface IExtractContract  {
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
        void onResponseData(boolean isSuccess, List<CodeBean> data);

    }

    /**
     * Presenter的接口
     */
    interface Presenter {


        /**
         * 请求数据
         */
        void requestData();
    }
}
