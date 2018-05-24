package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IRechargeSuccessContract {
	interface View extends BaseView {
		/**
		 * 返回数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onResponseData(boolean isSuccess, Object data);
	}

	interface Presenter {
		/**
		 * 请求数据
		 */
		void requestData();
	}
}
