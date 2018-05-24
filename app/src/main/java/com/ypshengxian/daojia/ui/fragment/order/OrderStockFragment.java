package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 已完成
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderStockFragment extends BaseYpFreshFragment {

    public static OrderStockFragment newInstance() {
        
        Bundle args = new Bundle();
        
        OrderStockFragment fragment = new OrderStockFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_stock;
    }

    @Override
    public void initView() {

    }
}
