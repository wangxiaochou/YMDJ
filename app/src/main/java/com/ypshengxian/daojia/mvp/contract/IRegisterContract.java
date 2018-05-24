package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.RegisterBean;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * Created by admin on 2018/3/19.
 */

public interface IRegisterContract {
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
        void onResponseData(boolean isSuccess, RegisterBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseCode(boolean isSuccess, SmsBean data);

        /**
         * 响应数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseResetBySms(boolean isSuccess, BaseModuleApiResult data);
    }

    /**
     * Presenter的接口
     */
    interface Presenter {
        /**
         * 注册
         *
         * @param password      密码
         * @param nickname      昵称
         * @param phone         电话
         * @param smsCode       验证码
         * @param registeredWay 终端
         * @param smsToken      验证码token
         */
        void bindPhone(String password, String nickname, String phone, String smsCode, String registeredWay, String smsToken);

        /**
         * 短信重置密码
         *
         * @param password 密码
         * @param phone    电话
         * @param smsCode  验证码
         * @param smsToken 验证码token
         */
        void resetBySms(String password, String phone, String smsCode, String smsToken);

        /**
         * 请求验证码
         *
         * @param map 数据
         */
        void getCode(Map<String, String> map);
    }
}
