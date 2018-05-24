package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class FlashBean extends BaseModuleApiResult {


    /**
     * id : 1
     * expiry : 1522903233
     * start : 1522051104
     * price : 0.50元
     * title : 测试商品A
     * unit : 斤
     * sold : 10
     * stock : 100
     * limit : 1
     * discount : 5.9
     * origin_price : 3.98元
     * img : https://www.ypshengxian.com/public/upload/goods/2018/02-24/574691d8ea8e686bb13c8b877928bd85.JPG
     */

    public int id;
    public int expiry;
    public int start;
    public String price;
    public String title;
    public String unit;
    public int sold;
    public int stock;
    public int limit;
    public String discount;
    public String origin_price;
    public String img;
}
