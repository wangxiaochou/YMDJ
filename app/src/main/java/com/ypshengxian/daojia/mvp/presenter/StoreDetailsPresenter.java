package com.ypshengxian.daojia.mvp.presenter;

import com.ypshengxian.daojia.base.BasePresenter;
import com.ypshengxian.daojia.mvp.contract.IStoreDetailsContract;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public class StoreDetailsPresenter extends BasePresenter<IStoreDetailsContract.View> implements IStoreDetailsContract.Presenter {

    @Override
    public void requestData(Map<String, String> map) {
    }
}
