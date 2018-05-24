package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;
import com.ypshengxian.daojia.network.bean.UploadBean;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IApplyAfterSalesContract {
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
		void onResponseData(boolean isSuccess, BaseModuleApiResult  data);
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
		 * 退款或申请售后
		 * @param orderSn 订单
		 * @param refundAmount 金额
		 * @param reason 原因
		 * @param id id
		 *
		 */
		void requestData( String orderSn, String refundAmount,String reason, String[] id);

		/**
		 * 上传头像
		 * @param fileByte 头像
		 *
		 */
		void uploadAvatar(String fileByte);

	}
}
