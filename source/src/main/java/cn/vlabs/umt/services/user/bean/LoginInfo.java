/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.umt.services.user.bean;

import java.io.Serializable;

import cn.vlabs.commons.principal.UserPrincipal;

/**
 * 包含用户登录成功信息，并返回用神马账户登陆成功的
 * @author lvly
 * @since 2013-2-4
 */
public class LoginInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 421558576001033051L;
	
	private User user;
	private LoginNameInfo loginNameInfo;
	private String passwordType;
	public static final String TYPE_COOKIE="password_cookie";
	public static final String TYPE_UMT="password_umt";
	public static final String TYPE_CORE_MAIL="password_core_mail";
	public static final String TYPE_THIRD_PARTY_SINA="password_third_party_sina";
	public static final String TYPE_THIRD_PARTY_QQ="password_third_party_qq";
	public static final String TYPE_THIRD_PARTY_CAS_HQ = "password_third_party_cas_hq";
	public static final String TYPE_THIRD_PARTY_UAF= "password_third_party_uaf";
	public static final String TYPE_WEB_LOGIN="web_token_login";
	public LoginNameInfo getLoginNameInfo() {
		return loginNameInfo;
	}
	public void setLoginNameInfo(LoginNameInfo loginNameInfo) {
		this.loginNameInfo = loginNameInfo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPasswordType() {
		return passwordType;
	}
	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}
	/**
	 * 为了兼容老接口，最好不要用这个方法，里面的值都在user提取就好
	 * @return
	 */
	public UserPrincipal getUserPrincipal(){
		if(this.user==null){
			return null;
		}
		return user.getUserPrincipal();
	}
	

}