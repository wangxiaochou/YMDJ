package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.AddCartBean;
import com.ypshengxian.daojia.network.bean.GoodsBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe
 */

public interface IGoodsDetailsContract {
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
		void onResponseData(boolean isSuccess, GoodsBean data);

		/**
		 * 响应数据
		 *
		 * @param isSuccess 是否成功
		 * @param data 数据
		 */
		void onAddCart(boolean isSuccess, AddCartBean data);
	}

	/**
	 * Presenter的接口
	 */
	interface Presenter {
		/**
		 * 请求数据
		 * @param goodsId   id
		 */
		void requestData(String goodsId);

		/**
		 * 添加购物车
		 *
		 * @param map 数据
		 */
		void addCart(Map<String, String> map);



	}
}
