package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.OrderBean;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserBean;
import com.ypshengxian.daojia.network.bean.UserCoinBean;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public interface IMineContract {
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
		void onResponseData(boolean isSuccess, UserBean data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onGetUserCoin(boolean isSuccess, UserCoinBean data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */

		void onGetOrder(boolean isSuccess, OrderBean data);

		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onUploadAvatar(boolean isSuccess, UploadBean data);
	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 请求数据
		 */
		void requestData();
		/**
		 * 请求数据
		 */
        void getUserCoin();
		/**
		 * 请求数据
		 */
        void getOrder();

		/**
		 * 上传头像
		 * @param fileByte 头像流
		 *
		 */
		void uploadAvatar(String fileByte);

	}
}
