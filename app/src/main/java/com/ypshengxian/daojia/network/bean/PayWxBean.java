package com.ypshengxian.daojia.network.bean;

import com.google.gson.annotations.SerializedName;
import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-04-05
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class PayWxBean  extends BaseModuleApiResult{


    /**
     * platform_data : {"appid":"wx63fdb89aff49f7fd","partnerid":"1501560351","prepayid":"wx05215116423597600ec6035c0695693625","package":"Sign=WXPay","noncestr":"5033ae514dcab59a623cafa9ed9d89a7","timestamp":1522936276,"sign":"A0CBFD563987BC5535D8DB85E28AAD08"}
     * platform : wechat
     */

    public PlatformDataBean platform_data;
    public String platform;

    public static class PlatformDataBean {
        /**
         * appid : wx63fdb89aff49f7fd
         * partnerid : 1501560351
         * prepayid : wx05215116423597600ec6035c0695693625
         * package : Sign=WXPay
         * noncestr : 5033ae514dcab59a623cafa9ed9d89a7
         * timestamp : 1522936276
         * sign : A0CBFD563987BC5535D8DB85E28AAD08
         */

        public String appid;
        public String partnerid;
        public String prepayid;
        @SerializedName("package")
        public String packageX;
        public String noncestr;
        public String timestamp;
        public String sign;
    }
}
