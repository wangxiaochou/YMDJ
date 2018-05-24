package com.ypshengxian.daojia.network.bean;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-27
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class CateGoryBean {


    /**
     * id : 5
     * code : hxsc
     * name : 海鲜水产
     * icon :
     * path :
     * isShow : 1
     * weight : 4
     * groupId : 1
     * parentId : 0
     * orgId : 1
     * orgCode : 1.
     * description :
     * depth : 1
     * children : [{"id":"6","code":"hx","name":"海鲜","icon":"","path":"","isShow":"1","weight":"0","groupId":"1","parentId":"5","orgId":"1","orgCode":"1.","description":"","depth":2,"children":[]}]
     */

    public String id;
    public String code;
    public String name;
    public String icon;
    public String path;
    public String isShow;
    public String weight;
    public String groupId;
    public String parentId;
    public String orgId;
    public String orgCode;
    public String description;
    public int depth;
    /** 是否选中 */
    public boolean isSelect=false;
    public List<ChildrenBean> children;

    public static class ChildrenBean {
        /**
         * id : 6
         * code : hx
         * name : 海鲜
         * icon :
         * path :
         * isShow : 1
         * weight : 0
         * groupId : 1
         * parentId : 5
         * orgId : 1
         * orgCode : 1.
         * description :
         * depth : 2
         * children : []
         */

        public String id;
        public String code;
        public String name;
        public String icon;
        public String path;
        public String isShow;
        public String weight;
        public String groupId;
        public String parentId;
        public String orgId;
        public String orgCode;
        public String description;
        public int depth;
        public boolean isSelect=false;
        public List<?> children;
    }
}
