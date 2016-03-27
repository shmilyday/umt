/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
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
package cn.vlabs.umt.ui;

/**
 * 这里定义一些属性值的Key的常量定义
 * 
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 3, 2009 3:46:35 PM
 */
public final class Attributes {
	private Attributes() {
	}

	/**
	 * Spring Context 变量名
	 */
	public static final String APPLICATION_CONTEXT_KEY = "cn.vlabs.umt.APPLICATION_CONTEXT";
	/**
	 * 登录以后存放在Session中的用户信息
	 */
	public static final String LOGIN_INFO = "loginInfo";

	/**
	 * 存储备激活邮箱
	 * */
	public static final String USER_TEMP_SECURITY_EMAIL = "tempSecurityEmail";
	/**
	 * 更改状态中的主登录邮箱
	 * */
	public static final String USER_PRIMARY_EMAIL = "primaryEmail";
	/**
	 * 用户激活的辅助邮箱
	 * */
	public static final String USER_SECONDARY_EMAIL = "secondaryEmail";
	/** 
	 * 用户的Ldap账号密码 
	 * */
	public static final String USER_LDAP_NAME="ldapName";

	/**
	 * 用户未激活的辅助邮箱
	 * */
	public static final String USER_TEMP_SECONDARY_EMAIL = "tempSecondaryEmail";
	/**
	 * 用户邮箱是否激活
	 * */
	public static final String IS_USER_LOGIN_ACTIVE = "isLoginEmailActive";
	/**
	 * PCookie的名称
	 */
	public static final String COOKIE_NAME = "PCookie";

	/**
	 * cookie中明文用户名
	 * */
	public static final String AUTO_FILL = "AUTO_FILL";
	/**
	 * 单点登录和退出使用的URL Parameter 名称
	 */
	public static final String RETURN_URL = "WebServerURL";
	public static final String SESSION_ID_KEY = "sid";
	public static final String LOGOUT_URL = "logoutURL";
	public static final String UMT_VERSION = "umtVersion";

	public static final String APP_TYPE = "appType";
	public static final String APP_REGISTER_URL_KEY = "registerUrl";
	public static final String APP_DEPUTY_LOGIN_RESULT_KEY = "loginResult";
	public static final String APP_THEME = "theme";
	public static final String TARGET = "target";

	/**
	 * 单点登录和退出，使用的App Parameter的名称
	 */
	public static final String APP_NAME = "appname";
	/**
	 * 单点登录时的Ticket
	 */
	public static final String TICKET = "ticket";
	/**
	 * 用户身份信息
	 */
	public static final String ROLE = "cn.vlabs.umt.role";
	/**
	 * 站点登录信息
	 */
	public static final String SITE_INFO = "cn.vlabs.umt.siteinfo";
	public static final String THIRDPARTY_CREDENTIAL = "_thirdpartycredential";

	/**
	 * 我访问过的appList
	 * */
	public static final String MY_APP_LIST = "myAppList";
	/**
	 * 第三方登陆返回的一些信息
	 * */
	public static final String THIRDPARTY_ACCESS_TOKEN = "thirdParty_access_token";
	public static final String THIRDPARTY_USER = "thirdParty_user";
	public static final String THIRDPARTY_EMAIL = "thirdParty_email";
	public static final String THIRDPARTY_CODE = "thirdParty_code";
	public static final String THIRDPARTY_OPEN_ID = "thirdParty_openId";
	public static final String THIRDPARTY_TYPE = "thirdParty_type";
	public static final String THIRDPARTY_URL = "thirdParty_url";
	public static final String THIRDPARTY_LOGIN_NAME = "thirdParty_login_name";

	public static final String SSO_FLAG = "UMTID";
	public static final String SSO_FLAG_DOMAIN = "escience.cn";

	public static final String[] SSO_PARAMS = { APP_NAME, RETURN_URL,
			LOGOUT_URL, APP_REGISTER_URL_KEY, SESSION_ID_KEY, APP_THEME,
			TARGET, UMT_VERSION };

}