package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.LoginBean;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * 登陆
 * Created by Yan on 2018/3/19.
 *
 * @author Yan
 */

public interface ILoginContract {
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
		void responseCode(boolean isSuccess, SmsBean data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 *
		 */
		void onLogin(boolean isSuccess, LoginBean data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 *
		 */

		void onRequestData(boolean isSuccess, LoginBean data);

	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 验证码登录
		 *
		 * @param userPhone 手机号
		 * @param code      验证码
		 * @param smsToken  验证码token
		 */
		void requestData(String userPhone, String code,String smsToken);

		/**
		 * 获取验证码
		 *
		 * @param map
		 */
		void requestCode(Map<String, String> map);

		/**
		 * 登录
		 *
		 * @param phone 用户名
		 * @param pass  密码
		 */
		void login(String phone, String pass);

		/**
		 * 第三方登录
		 *
		 * @param oauthCode OAuth授权中的code参数
		 * @param channel   第三方登录渠道
		 *
		 */
		void otherLogin(String oauthCode, String channel);
	}
}
