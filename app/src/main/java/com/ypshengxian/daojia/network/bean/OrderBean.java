package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * 订单
 *
 * @author Yan
 * @date 2018-04-01
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class OrderBean extends BaseModuleApiResult {


    /**
     * cart : 1
     * unpaid : 61
     * paid : 0
     * success : 3
     * refunding : 7
     * refunded : 31
     */

    public int cart;
    public int unpaid;
    public int paid;
    public int success;
    public int refunding;
    public int refunded;
}
