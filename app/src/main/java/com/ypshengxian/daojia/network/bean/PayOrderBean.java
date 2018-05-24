package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 订单
 *
 * @author 订单
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PayOrderBean extends BaseModuleApiResult {


    /**
     * deliveryDay : 2018-04-09
     * orderInfo : {"goods":[{"goods_id":"298","goods_name":"红苋菜 约500g","goods_price":"2.58","goods_unit":"g","goods_pre_price":"2.58","goods_num":"2","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172919f6da54629298.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"300","goods_name":"油麦菜 约500g","goods_price":"1.58","goods_unit":"g","goods_pre_price":"1.58","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/1729200d1e43818718.jpg?version=8.2.20","goods_origin_price":"1.99","selected":"1"},{"goods_id":"88","goods_name":"菠菜 约500g","goods_price":"2.88","goods_unit":"g","goods_pre_price":"2.88","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172636c7edd2753651.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"101","goods_name":"卷心包 约1kg","goods_price":"0.90","goods_unit":"g","goods_pre_price":"0.45","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264337c3de221924.jpg?version=8.2.20","goods_origin_price":"3.50","selected":"1"},{"goods_id":"106","goods_name":"有机花菜 约500g","goods_price":"1.99","goods_unit":"g","goods_pre_price":"1.99","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264664720e414874.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"108","goods_name":"棵白菜 约500g","goods_price":"0.89","goods_unit":"g","goods_pre_price":"0.89","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264777c79f430041.jpg?version=8.2.20","goods_origin_price":"1.58","selected":"1"},{"goods_id":"109","goods_name":"生菜 约500g（限购1份）","goods_price":"0.01","goods_unit":"g","goods_pre_price":"0.01","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/1726488022e9267878.jpg?version=8.2.20","goods_origin_price":"1.68","selected":"1"},{"goods_id":"115","goods_name":"芦蒿 约300g","goods_price":"1.50","goods_unit":"g","goods_pre_price":"2.50","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172651b15434431060.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"145","goods_name":"菜苔 约500g","goods_price":"2.19","goods_unit":"g","goods_pre_price":"2.19","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172707b89d53132616.jpg?version=8.2.20","goods_origin_price":"2.98","selected":"1"},{"goods_id":"146","goods_name":"散装娃娃菜 约500g","goods_price":"1.18","goods_unit":"g","goods_pre_price":"1.18","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172708c2045c401755.jpg?version=8.2.20","goods_origin_price":"3.00","selected":"1"},{"goods_id":"186","goods_name":"板栗 约500g","goods_price":"3.99","goods_unit":"g","goods_pre_price":"3.99","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17273358ad04867151.jpg?version=8.2.20","goods_origin_price":"4.98","selected":"1"}],"total_price":22.270000000000003}
     * shop : {"id":"8","title":"岸上玫瑰店","phone":"13349112049","rootCityId":"11588","secCityId":"11589","trdCityId":"11592","address":"岸上玫瑰北区","lat":"31.806658274502094","lng":"117.21661895513535","status":"published","start_time":"00:00:00","end_time":"23:59:59","radius":"3000","about":"<p>翡翠路与龙图路交口东20米处（岸上玫瑰北区南门）<\/p>\n","image":"","fullAddress":"安徽省合肥市蜀山区岸上玫瑰北区"}
     * packets : [{"id":"3","title":"标准购物袋","price":"0.30","seq":"1"}]
     * coupon :
     */

    public String deliveryDay;
    public OrderInfoBean orderInfo;
    public ShopBean shop;
    public String coupon;
    public List<PacketsBean> packets;

    public static class OrderInfoBean {
        /**
         * goods : [{"goods_id":"298","goods_name":"红苋菜 约500g","goods_price":"2.58","goods_unit":"g","goods_pre_price":"2.58","goods_num":"2","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172919f6da54629298.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"300","goods_name":"油麦菜 约500g","goods_price":"1.58","goods_unit":"g","goods_pre_price":"1.58","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/1729200d1e43818718.jpg?version=8.2.20","goods_origin_price":"1.99","selected":"1"},{"goods_id":"88","goods_name":"菠菜 约500g","goods_price":"2.88","goods_unit":"g","goods_pre_price":"2.88","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172636c7edd2753651.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"101","goods_name":"卷心包 约1kg","goods_price":"0.90","goods_unit":"g","goods_pre_price":"0.45","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264337c3de221924.jpg?version=8.2.20","goods_origin_price":"3.50","selected":"1"},{"goods_id":"106","goods_name":"有机花菜 约500g","goods_price":"1.99","goods_unit":"g","goods_pre_price":"1.99","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264664720e414874.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"108","goods_name":"棵白菜 约500g","goods_price":"0.89","goods_unit":"g","goods_pre_price":"0.89","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17264777c79f430041.jpg?version=8.2.20","goods_origin_price":"1.58","selected":"1"},{"goods_id":"109","goods_name":"生菜 约500g（限购1份）","goods_price":"0.01","goods_unit":"g","goods_pre_price":"0.01","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/1726488022e9267878.jpg?version=8.2.20","goods_origin_price":"1.68","selected":"1"},{"goods_id":"115","goods_name":"芦蒿 约300g","goods_price":"1.50","goods_unit":"g","goods_pre_price":"2.50","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172651b15434431060.jpg?version=8.2.20","goods_origin_price":"3.98","selected":"1"},{"goods_id":"145","goods_name":"菜苔 约500g","goods_price":"2.19","goods_unit":"g","goods_pre_price":"2.19","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172707b89d53132616.jpg?version=8.2.20","goods_origin_price":"2.98","selected":"1"},{"goods_id":"146","goods_name":"散装娃娃菜 约500g","goods_price":"1.18","goods_unit":"g","goods_pre_price":"1.18","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/172708c2045c401755.jpg?version=8.2.20","goods_origin_price":"3.00","selected":"1"},{"goods_id":"186","goods_name":"板栗 约500g","goods_price":"3.99","goods_unit":"g","goods_pre_price":"3.99","goods_num":"1","goods_img":"http://admin.ypshengxian.com/files/goods/2018/04-07/17273358ad04867151.jpg?version=8.2.20","goods_origin_price":"4.98","selected":"1"}]
         * total_price : 22.270000000000003
         */

        public float total_price;
        public List<GoodsBean> goods;

        public static class GoodsBean {
            /**
             * goods_id : 298
             * goods_name : 红苋菜 约500g
             * goods_price : 2.58
             * goods_unit : g
             * goods_pre_price : 2.58
             * goods_num : 2
             * goods_img : http://admin.ypshengxian.com/files/goods/2018/04-07/172919f6da54629298.jpg?version=8.2.20
             * goods_origin_price : 3.98
             * selected : 1
             */

            public String goods_id;
            public String goods_name;
            public String goods_price;
            public String goods_unit;
            public String goods_pre_price;
            public String goods_num;
            public String goods_img;
            public String goods_origin_price;
            public String selected;
        }
    }

    public static class ShopBean {
        /**
         * id : 8
         * title : 岸上玫瑰店
         * phone : 13349112049
         * rootCityId : 11588
         * secCityId : 11589
         * trdCityId : 11592
         * address : 岸上玫瑰北区
         * lat : 31.806658274502094
         * lng : 117.21661895513535
         * status : published
         * start_time : 00:00:00
         * end_time : 23:59:59
         * radius : 3000
         * about : <p>翡翠路与龙图路交口东20米处（岸上玫瑰北区南门）</p>

         * image :
         * fullAddress : 安徽省合肥市蜀山区岸上玫瑰北区
         */

        public String id;
        public String title;
        public String phone;
        public String rootCityId;
        public String secCityId;
        public String trdCityId;
        public String address;
        public String lat;
        public String lng;
        public String status;
        public String start_time;
        public String end_time;
        public String radius;
        public String about;
        public String image;
        public String fullAddress;
    }

    public static class PacketsBean {
        /**
         * id : 3
         * title : 标准购物袋
         * price : 0.30
         * seq : 1
         */

        public String id;
        public String title;
        public String price;
        public String seq;
    }
}
