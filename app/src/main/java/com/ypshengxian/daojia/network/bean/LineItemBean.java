package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 订单详情
 *
 * @author Yan
 * @date 2018-04-05
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LineItemBean extends BaseModuleApiResult {


    /**
     * deliveryDay : 2018-04-06
     * sn : 2018040517381234211
     * createTime : 2018-4-05 17:38
     * status : paid
     * packet : {"name":"大号购物袋","price":"10"}
     * shop : {"id":"4","title":"固镇路店","address":"安徽省合肥市庐阳区海亮红玺台","phone":"15395007659"}
     * coupons : []
     * items : [{"title":"豌豆豌豆","price":"0.01","prePrice":"1.22","unit":"箱","num":"1","img":"http://admin.ypshengxian.com/files/tmp/2018/03-14/220119feeb8e672159.png?version=8.2.20"}]
     * price_amount : 11
     */

    public String deliveryDay;

    public String deliveryTime;

    public String sn;
    public String createTime;
    public String status;
    public PacketBean packet;
    public ShopBean shop;
    public String priceAmount;
    public Object coupons;
    public List<ItemsBean> items;

    public static class PacketBean {
        /**
         * name : 大号购物袋
         * price : 10
         */

        public String name;
        public String price;
    }

    public static class ShopBean {
        /**
         * id : 4
         * title : 固镇路店
         * address : 安徽省合肥市庐阳区海亮红玺台
         * phone : 15395007659
         */

        public String id;
        public String title;
        public String address;
        public String phone;
    }

    public static class ItemsBean {
        /**
         * title : 豌豆豌豆
         * price : 0.01
         * prePrice : 1.22
         * unit : 箱
         * num : 1
         * img : http://admin.ypshengxian.com/files/tmp/2018/03-14/220119feeb8e672159.png?version=8.2.20
         */

        public String title;
        public String price;
        public String prePrice;
        public String unit;
        public String num;
        public String img;
    }
}
