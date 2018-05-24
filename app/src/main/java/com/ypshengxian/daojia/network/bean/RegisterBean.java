package com.ypshengxian.daojia.network.bean;

import com.ypshengxian.daojia.network.base.BaseModuleApiResult;

import java.util.List;

/**
 * @author ZSH
 * @create 2018/3/26
 * @Describe
 */

public class RegisterBean extends BaseModuleApiResult{

	/**
	 * token : srphdjtgws0sos8k088sk4osos8gs88
	 * user : {"id":"2","uri":"","nickname":"测试管理员","type":"default","smallAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b9364c302793.jpg","mediumAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b8d318520934.jpg","largeAvatar":"http://yp-new.test.com/files/default/2018/03-12/133419b85254471018.jpg","roles":["ROLE_USER","ROLE_SUPER_ADMIN"],"locked":"0","distributorToken":"","uuid":"8d3a8c9cdaa461bf0d4ef2b751418b5796f20770","currentIp":"172.20.0.1","gender":"secret","iam":"","birthday":null}
	 */

	private String token;
	private UserBean user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public static class UserBean {
		/**
		 * id : 2
		 * uri :
		 * nickname : 测试管理员
		 * type : default
		 * smallAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b9364c302793.jpg
		 * mediumAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b8d318520934.jpg
		 * largeAvatar : http://yp-new.test.com/files/default/2018/03-12/133419b85254471018.jpg
		 * roles : ["ROLE_USER","ROLE_SUPER_ADMIN"]
		 * locked : 0
		 * distributorToken :
		 * uuid : 8d3a8c9cdaa461bf0d4ef2b751418b5796f20770
		 * currentIp : 172.20.0.1
		 * gender : secret
		 * iam :
		 * birthday : null
		 */

		private String id;
		private String uri;
		private String nickname;
		private String type;
		private String smallAvatar;
		private String mediumAvatar;
		private String largeAvatar;
		private String locked;
		private String distributorToken;
		private String uuid;
		private String currentIp;
		private String gender;
		private String iam;
		private String birthday;
		private List<String> roles;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getSmallAvatar() {
			return smallAvatar;
		}

		public void setSmallAvatar(String smallAvatar) {
			this.smallAvatar = smallAvatar;
		}

		public String getMediumAvatar() {
			return mediumAvatar;
		}

		public void setMediumAvatar(String mediumAvatar) {
			this.mediumAvatar = mediumAvatar;
		}

		public String getLargeAvatar() {
			return largeAvatar;
		}

		public void setLargeAvatar(String largeAvatar) {
			this.largeAvatar = largeAvatar;
		}

		public String getLocked() {
			return locked;
		}

		public void setLocked(String locked) {
			this.locked = locked;
		}

		public String getDistributorToken() {
			return distributorToken;
		}

		public void setDistributorToken(String distributorToken) {
			this.distributorToken = distributorToken;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getCurrentIp() {
			return currentIp;
		}

		public void setCurrentIp(String currentIp) {
			this.currentIp = currentIp;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getIam() {
			return iam;
		}

		public void setIam(String iam) {
			this.iam = iam;
		}

		public Object getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}
	}

	@Override
	public String toString() {
		return "RegisterBean{" +
				"token='" + token + '\'' +
				", user=" + user +
				'}';
	}
}
