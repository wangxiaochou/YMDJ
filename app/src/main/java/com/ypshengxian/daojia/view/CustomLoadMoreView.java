package com.ypshengxian.daojia.view;


import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.ypshengxian.daojia.R;

/**
 *
 *
 * @author Yan
 * @date 2016/10/15
 */
public final class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
