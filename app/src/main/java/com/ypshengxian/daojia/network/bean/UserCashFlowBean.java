package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 实体
 *
 * @author
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class UserCashFlowBean extends BaseModuleApiResult {

    /**
     * start : 0
     * limit : 10
     * total : 1
     * data :
     */

    public int start;
    public int limit;
    public int total;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * title : 商品X1
         * created_time : 2018-4-16 05:11
         * amount : 100
         * type : outflow
         */

        public String title;
        public String created_time;
        public String amount;
        public String type;
    }
}
