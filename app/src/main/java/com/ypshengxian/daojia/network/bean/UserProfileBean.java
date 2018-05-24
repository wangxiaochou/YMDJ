package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 页面
 *
 * @author Yan
 * @date 2018-03-31
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class UserProfileBean extends BaseModuleApiResult {

    /**
     * id : 2
     * uri :
     * nickname : 测试管理员
     * type : default
     * smallAvatar : http://admin.ypshengxian.com/files/user/2018/03-30/18462732f4e9864451.png
     * mediumAvatar : http://admin.ypshengxian.com/files/user/2018/03-30/18462732e0eb901031.png
     * largeAvatar : http://admin.ypshengxian.com/files/user/2018/03-30/18462732ca90805081.png
     * roles : ["ROLE_USER","ROLE_SUPER_ADMIN"]
     * locked : 0
     * uuid : 8d3a8c9cdaa461bf0d4ef2b751418b5796f20770
     */

    public String id;
    public String uri;
    public String nickname;
    public String type;
    public String smallAvatar;
    public String mediumAvatar;
    public String largeAvatar;
    public String locked;
    public String uuid;
    public List<String> roles;
}
