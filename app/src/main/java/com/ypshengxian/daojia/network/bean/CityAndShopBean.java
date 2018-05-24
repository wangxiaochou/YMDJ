package com.ypshengxian.daojia.network.bean;

import java.util.ArrayList;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-12
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class CityAndShopBean {
    public ArrayList<CityBean>  cityBeans;
    public ArrayList<ShopSmallBean> shopSmallBeans;

    public CityAndShopBean(ArrayList<CityBean> cityBeans, ArrayList<ShopSmallBean> shopSmallBeans) {
        this.cityBeans = cityBeans;
        this.shopSmallBeans = shopSmallBeans;
    }
}
