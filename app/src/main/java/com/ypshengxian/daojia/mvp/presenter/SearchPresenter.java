package com.ypshengxian.daojia.mvp.presenter;

import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.ISearchContract;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class SearchPresenter extends BasePresenter<ISearchContract.View> implements ISearchContract.Presenter {
	@Override
	public void requestData(String text) {

	}
}
