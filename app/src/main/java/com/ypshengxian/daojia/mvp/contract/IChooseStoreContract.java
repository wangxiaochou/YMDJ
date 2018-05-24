package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.CityBean;
import com.ypshengxian.daojia.network.bean.ShopBean;

import java.util.ArrayList;

/**
 * @author ZSH
 * @create 2018/3/21
 * @Describe
 */

public interface IChooseStoreContract {
	/**
	 * View的接口
	 */
	interface View extends BaseView {
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data 数据
		 */
		void onResponseData(boolean isSuccess,ArrayList<ShopBean> data);
		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data 数据
		 */
		void onGetCity(boolean isSuccess, ArrayList<CityBean>  data);
	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 请求数据
		 *
		 *
		 */
		void requestData();

		/**
		 * 获得城市列表
		 *
		 */
		void getCity();
	}
}
