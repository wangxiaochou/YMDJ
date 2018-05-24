package com.ypshengxian.daojia.ui.fragment.order;

import android.os.Bundle;

import com.ypshengxian.daojia.R;
import com.ypshengxian.daojia.base.BaseYpFreshFragment;

/**
 * 待自提
 *
 * @author Yan
 * @date 2018-03-22
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class OrderPickFragment extends BaseYpFreshFragment {


    public static OrderPickFragment newInstance() {

        Bundle args = new Bundle();

        OrderPickFragment fragment = new OrderPickFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentView() {
        return R.layout.fragment_order_pick;
    }

    @Override
    public void initView() {

    }
}
