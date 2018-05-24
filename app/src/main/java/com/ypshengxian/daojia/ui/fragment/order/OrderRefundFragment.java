package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 退款
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderRefundFragment extends BaseYpFreshFragment {
    public static OrderRefundFragment newInstance() {

        Bundle args = new Bundle();

        OrderRefundFragment fragment = new OrderRefundFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_refund;
    }

    @Override
    public void initView() {

    }
}
