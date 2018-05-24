package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 待付款
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderPaymentFragment extends BaseYpFreshFragment {


    public static OrderPaymentFragment newInstance() {

        Bundle args = new Bundle();

        OrderPaymentFragment fragment = new OrderPaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_payment;
    }

    @Override
    public void initView() {

    }
}
