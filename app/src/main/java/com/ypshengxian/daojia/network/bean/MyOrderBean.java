package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 我的订单
 *
 * @author Yan
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class MyOrderBean extends BaseModuleApiResult {


    /**
     * start : 0
     * limit : 10
     * total : 1
     * data : [{"id":"300","sn":"2018040622570938943","pay_amount":"2","status":"paid","pay_time":"1523026638","item":[{"id":"1","title":"脐橙 约1kg","subtitle":"精品水果  新鲜直达","parentId":"0","attribute":"","sn":"2110333","maxPurchase":"0","formatNum":"1000","stock":"18","price":"9.16","prePrice":"4.58","originPrice":"12.00","salesSum":"296","categoryId":"13832","about":"","thumb":"http://admin.ypshengxian.com/assets/img/default/goods.jpg","picture":["http://admin.ypshengxian.com/files/goods/2018/04-07/172547bac3c8311692.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172547bd7628694240.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172548c0b7f1825764.jpg?version=8.2.20"],"sortSeq":"0","activityType":null,"activityId":null,"unit":"g","num":"2","pay_amount":"2"}],"refund_status":""}]
     */

    public int start;
    public int limit;
    public int total;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * id : 300
         * sn : 2018040622570938943
         * pay_amount : 2
         * status : paid
         * pay_time : 1523026638
         * item : [{"id":"1","title":"脐橙 约1kg","subtitle":"精品水果  新鲜直达","parentId":"0","attribute":"","sn":"2110333","maxPurchase":"0","formatNum":"1000","stock":"18","price":"9.16","prePrice":"4.58","originPrice":"12.00","salesSum":"296","categoryId":"13832","about":"","thumb":"http://admin.ypshengxian.com/assets/img/default/goods.jpg","picture":["http://admin.ypshengxian.com/files/goods/2018/04-07/172547bac3c8311692.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172547bd7628694240.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172548c0b7f1825764.jpg?version=8.2.20"],"sortSeq":"0","activityType":null,"activityId":null,"unit":"g","num":"2","pay_amount":"2"}]
         * refund_status :
         */

        public String id;
        public String sn;
        public String pay_amount;
        public String status;
        public String pay_time;
        public String refund_status;
        public List<ItemBean> item;

        public static class ItemBean {
            /**
             * id : 1
             * title : 脐橙 约1kg
             * subtitle : 精品水果  新鲜直达
             * parentId : 0
             * attribute :
             * sn : 2110333
             * maxPurchase : 0
             * formatNum : 1000
             * stock : 18
             * price : 9.16
             * prePrice : 4.58
             * originPrice : 12.00
             * salesSum : 296
             * categoryId : 13832
             * about :
             * thumb : http://admin.ypshengxian.com/assets/img/default/goods.jpg
             * picture : ["http://admin.ypshengxian.com/files/goods/2018/04-07/172547bac3c8311692.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172547bd7628694240.jpg?version=8.2.20","http://admin.ypshengxian.com/files/goods/2018/04-07/172548c0b7f1825764.jpg?version=8.2.20"]
             * sortSeq : 0
             * activityType : null
             * activityId : null
             * unit : g
             * num : 2
             * pay_amount : 2
             */

            public String id;
            public String title;
            public String subtitle;
            public String parentId;
            public String attribute;
            public String sn;
            public String maxPurchase;
            public String formatNum;
            public String stock;
            public String price;
            public String prePrice;
            public String originPrice;
            public String salesSum;
            public String categoryId;
            public String about;
            public String thumb;
            public String sortSeq;
            public Object activityType;
            public Object activityId;
            public String unit;
            public String num;
            public String pay_amount;
            public List<String> picture;
        }
    }
}
