package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public class UserBean extends BaseModuleApiResult{


	/**
	 * id : 2
	 * uri :
	 * nickname : 测试管理员
	 * type : default
	 * smallAvatar : http://admin.ypshengxian.com/files/default/2018/03-12/133419b9364c302793.jpg
	 * mediumAvatar : http://admin.ypshengxian.com/files/default/2018/03-12/133419b8d318520934.jpg
	 * largeAvatar : http://admin.ypshengxian.com/files/default/2018/03-12/133419b85254471018.jpg
	 * roles : ["ROLE_USER","ROLE_SUPER_ADMIN"]
	 * locked : 0
	 * uuid : 8d3a8c9cdaa461bf0d4ef2b751418b5796f20770
	 * gender : secret
	 * birthday : null
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
	public String gender;
	public String birthday;
	public String verifiedMobile;
	public List<String> roles;
}
