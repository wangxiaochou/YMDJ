package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface ISetPaymentPasswordContract {
    interface View extends BaseView {
        /**
         * 返回数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseData(boolean isSuccess, Object data);

        /**
         * 返回数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseCode(boolean isSuccess, SmsBean data);
    }

    interface Presenter {
        /**
         * 请求数据
         */
        void requestData(String phone, String smsCode, String payPassword,String smsToken);

        void getCode(Map<String, String> map);
    }
}
