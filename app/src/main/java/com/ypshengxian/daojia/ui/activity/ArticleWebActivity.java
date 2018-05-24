package com.ypshengxian.daojia.ui.activity;


import android.os.Bundle;
import android.webkit.WebView;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.YpCache;
import com.ypshengxian.daojia.base.BaseMVPYpFreshActicity;
import com.ypshengxian.daojia.mvp.contract.IArticleContract;
import com.ypshengxian.daojia.mvp.presenter.ArticlePresenter;
import com.ypshengxian.daojia.network.bean.ArticleBean;
import com.ypshengxian.daojia.utils.StringUtils;
import com.ypshengxian.daojia.utils.TitleUtils;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author ZSH
 * @create 2018/3/20
 * @Describe 文章
 */
public class ArticleWebActivity extends BaseMVPYpFreshActicity<IArticleContract.View, ArticlePresenter> implements IArticleContract.View {
    /** 视图WEb */
    private WebView webview;

    @Override
    public void onResponseData(boolean isSuccess, ArticleBean data) {
        if (isSuccess) {
            TitleUtils.setTitleBar(this, data.title);
            if (!StringUtils.isEmpty(data.body)) {
                webview.loadData(getHtmlData(data.body).replaceAll("//", "http://"), "text/html; charset=UTF-8", null);
            }
        }
    }

    /**
     * 将富文本转化成HTML
     *
     * @param bodyHTML body内容
     * @return html字符串
     */

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{width: 100%;height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    @Override
    protected ArticlePresenter createPresenter() {
        return new ArticlePresenter();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_article_web;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        webview = (WebView) findViewById(R.id.web_activity_article);
        Map<String, String> map = new WeakHashMap<>();
        map.put("id", "2");
        YpCache.setMap(map);
        mPresenter.requestData(map);
    }


}
