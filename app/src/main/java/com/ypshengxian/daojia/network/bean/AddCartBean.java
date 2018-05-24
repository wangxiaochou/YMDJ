package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-29
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class AddCartBean extends BaseModuleApiResult {

    /**
     * goods : [{"goods_id":"1","goods_name":"豌豆豌豆","goods_price":"2.69元","goods_unit":"箱","goods_pre_price":"1.22","goods_num":"2","goods_img":"http://admin.ypshengxian.com/files/tmp/2018/03-14/220119feeb8e672159.png?version=8.2.20","goods_origin_Price":"5.22","selected":"1"}]
     * total_price : 5.38
     */

    public double total_price;
    public List<GoodsBean> goods;

    public static class GoodsBean {
        /**
         * goods_id : 1
         * goods_name : 豌豆豌豆
         * goods_price : 2.69元
         * goods_unit : 箱
         * goods_pre_price : 1.22
         * goods_num : 2
         * goods_img : http://admin.ypshengxian.com/files/tmp/2018/03-14/220119feeb8e672159.png?version=8.2.20
         * goods_origin_Price : 5.22
         * selected : 1
         */

        public String goods_id;
        public String goods_name;
        public String goods_price;
        public String goods_unit;
        public String goods_pre_price;
        public String goods_num;
        public String goods_img;
        public String goods_origin_Price;
        public int selected;
        /** 是哦否选中 */
        public boolean isSelected=true;
    }
}
