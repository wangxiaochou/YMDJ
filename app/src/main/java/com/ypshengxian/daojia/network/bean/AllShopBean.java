package com.ypshengxian.daojia.network.bean;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-04
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class AllShopBean {

    public List<City> cityBeans;

    public AllShopBean(List<City> cityBeans) {
        this.cityBeans = cityBeans;
    }

    public static class City {

        public String name;
        public String id;
        public String parentId;
        public List<Shop> shops;

        public City(String name, String id, String parentId, List<Shop> shops) {
            this.name = name;
            this.id = id;
            this.parentId = parentId;
            this.shops = shops;
        }

        public static class Shop {

            public String name;
            /** 一级 */
            public String rootCityId;
            /** 二级 */
            public String secCityId;
            /** 三级 */
            public String trdCityId;
            /** 精度 */
            public String lat;
            /** 维度 */
            public String lng;
            public String id;

            public Shop(String name, String rootCityId, String secCityId, String trdCityId, String lat, String lng, String id) {
                this.name = name;
                this.rootCityId = rootCityId;
                this.secCityId = secCityId;
                this.trdCityId = trdCityId;
                this.lat = lat;
                this.lng = lng;
                this.id = id;
            }
        }
    }
}
