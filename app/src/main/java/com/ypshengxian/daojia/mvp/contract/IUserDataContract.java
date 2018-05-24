package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.UploadBean;
import com.ypshengxian.daojia.network.bean.UserProfileBean;

/**
 * @author ZSH
 * @create 2018/3/21
 * @Describe
 */

public interface IUserDataContract {
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
		void onResponseData(boolean isSuccess, Object data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onUploadAvatar(boolean isSuccess, UploadBean data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data      数据
		 */
		void onUpdateUserProfile(boolean isSuccess, UserProfileBean data);
	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 请求数据
		 *
		 */
		void requestData();

		/**
		 * 上传头像
		 * @param fileByte 头像
		 *
		 */
		void uploadAvatar(String fileByte);

		/**
		 *
		 * 上传用户资料
		 *
		 * @param name  昵称
		 * @param gender 性别
		 * @param id 图片iD
		 */
		void  updateUserProfile( String name, String gender,String birthday, String id);

	}
}
