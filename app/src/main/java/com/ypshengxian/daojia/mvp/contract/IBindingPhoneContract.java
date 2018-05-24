package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.SmsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/25
 * @Describe
 */

public interface IBindingPhoneContract {
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
		void onSendSms(boolean isSuccess, SmsBean data);

		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onBindPhone(boolean isSuccess,BaseModuleApiResult data);


	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 请求数据
		 *
		 * @param map   数据
		 */
		void sendSms(Map<String,String> map);

		/**
		 * 请求数据
		 * @param phone 手机号码
		 * @param code 验证码
		 * @param smsToken 验证码token
		 */
		void bindPhone(String phone,String smsToken,String code);



	}
}
