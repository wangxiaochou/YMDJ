package com.ypshengxian.daojia.network.base;

/**
 * 网络调用结果基类
 *
 * @author Yan
 * @date 2017.07.20
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public  class BaseModuleApiResult{

    /**
     * error : {"message":"错误描述信息。","code":"错误码（值为整形）"}
     */

    public ErrorBean error;



    public static class ErrorBean {
        /**
         * message : 错误描述信息。
         * code : 错误码（值为整形）
         */

        public String message;
        public String code;
    }
}
