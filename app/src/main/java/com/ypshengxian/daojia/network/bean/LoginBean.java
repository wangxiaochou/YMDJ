package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * 页面
 *
 * @author lenovo
 * @date 2018-03-25
 * @note -
 * ---------------------------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */


public class LoginBean extends BaseModuleApiResult {

	/**
	 * token : srphdjtgws0sos8k088sk4osos8gs88
	 * user : {"id":"2","uri":"","nickname":"测试管理员","type":"default","smallAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b9364c302793.jpg","mediumAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b8d318520934.jpg","largeAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b85254471018.jpg","roles":["ROLE_USER","ROLE_TEACHER","ROLE_SUPER_ADMIN"],"locked":"0","distributorToken":"","uuid":"8d3a8c9cdaa461bf0d4ef2b751418b5796f20770","currentIp":"172.20.0.1","gender":"secret","iam":"","birthday":null}
	 */

	public String token;
	public UserBean user;

	public static class UserBean {
		/**
		 * id : 2
		 * uri :
		 * nickname : 测试管理员
		 * type : default
		 * smallAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b9364c302793.jpg
		 * mediumAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b8d318520934.jpg
		 * largeAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b85254471018.jpg
		 * roles : ["ROLE_USER","ROLE_TEACHER","ROLE_SUPER_ADMIN"]
		 * locked : 0
		 * distributorToken :
		 * uuid : 8d3a8c9cdaa461bf0d4ef2b751418b5796f20770
		 * currentIp : 172.20.0.1
		 * gender : secret
		 * iam :
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
		public String distributorToken;
		public String uuid;
		public String currentIp;
		public String gender;
		public String iam;
		public Object birthday;
		/** 是否绑定手机号 */
		public String verifiedMobile;
		public List<String> roles;
	}

	@Override
	public String toString() {
		return "LoginBean{" +
				"token='" + token + '\'' +
				", user=" + user +
				'}';
	}
}
