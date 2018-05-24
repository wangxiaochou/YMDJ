package com.ypshengxian.daojia.event;

/**
 * 订单消息
 *
 * @author 订单消息
 * @date 2018-04-02
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class PayOrderEvent {
    public String money;
    public String title;
    public String id;

    public PayOrderEvent(String money,String title,String id) {
        this.money = money;
        this.title=title;
        this.id=id;
    }
}
