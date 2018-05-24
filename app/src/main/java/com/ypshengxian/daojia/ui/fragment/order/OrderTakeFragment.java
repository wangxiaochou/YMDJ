package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 待收货
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderTakeFragment extends BaseYpFreshFragment {

    public static OrderTakeFragment newInstance() {

        Bundle args = new Bundle();

        OrderTakeFragment fragment = new OrderTakeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_take;
    }

    @Override
    public void initView() {

    }
}
