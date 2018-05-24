package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public interface ISettingContract {
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
		void onResponseData(boolean isSuccess, BaseModuleApiResult data);
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
