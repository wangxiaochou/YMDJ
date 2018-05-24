package com.ypshengxian.daojia.event;

/**
 * 二级店铺ID
 *
 * @author Yan
 * @date 2018-04-19
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class ShopIdEvent {
    /** 二级城市Id */
    public String shopTwoId;

    public ShopIdEvent(String shopTwoId) {
        this.shopTwoId = shopTwoId;
    }
}
