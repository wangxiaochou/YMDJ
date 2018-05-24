package com.ypshengxian.daojia.mvp.contract;

import com.ypshengxian.daojia.base.BaseView;
import com.ypshengxian.daojia.network.bean.ArticleBean;

import java.util.Map;

/**
 * @author ZSH
 * @create 2018/3/22
 * @Describe
 */

public interface IArticleContract {

    interface View extends BaseView {
        /**
         * 返回数据
         *
         * @param isSuccess 是否成功
         * @param data      数据
         */
        void onResponseData(boolean isSuccess, ArticleBean data);
    }

    interface Presenter {
        /**
         * 请求数据
         *
         * @param map 数据
         */
        void requestData(Map<String, String> map);
    }
}
