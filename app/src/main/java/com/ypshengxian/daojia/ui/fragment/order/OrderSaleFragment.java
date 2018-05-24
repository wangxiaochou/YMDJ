package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 售后
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderSaleFragment extends BaseYpFreshFragment {

    public static OrderSaleFragment newInstance() {

        Bundle args = new Bundle();

        OrderSaleFragment fragment = new OrderSaleFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_sale;
    }

    @Override
    public void initView() {

    }
}
