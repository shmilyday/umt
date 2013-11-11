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
package cn.vlabs.duckling.api.umt.sso.configable.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.commons.principal.UserPrincipalV7;
import cn.vlabs.duckling.api.umt.sso.configable.msg.Message;

/**
 * 用户扩展类，须实现回调函数
 * @date 2013-2-5
 * @author LvLongYun
 */
public interface ILoginHandle {
	static final String UMT_LOGIN_URL_KEY = "umt.login.url"; 
	static final String UMT_LOGOUT_URL_KEY = "umt.logout.url"; 
	static final String UMT_PUBLICKEY_URL_KEY = "umt.publicKey.url"; 
	static final String LOCALAPP_LOGOUT_URL = "localapp.logout.url"; 
	static final String LOCALAPP_LOGIN_RETURN = "localapp.login.return"; 
	static final String UMT_AUTH_APPNAME_KEY = "umt.auth.appname";
	static final String UMT_AUTH_RULE_KEY = "umt.auth.IgnoreFile";
	static final String UMT_LOGIN_EXTHANDLE_CLASS = "umt.login.exthandle.class";
	static final String APP_REGISTER_URL_KEY = "registerUrl";
	static final String DEFAULT_IMPL_PATH="cn.vlabs.duckling.api.umt.sso.configable.service.impl.DefaultLoginHandleImpl";
	/**
	 * 用户自定义回调函数<br>
	 * 用户登录成功后，初始化UserContext的额外信息<br>
     * 例如：<br>
     * 添加用户的principals
	 * @param request
	 * @param response
	 * @param context
	 */
	void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,UserPrincipalV7 up);
	/**
	 * 用户自定义回调函数，登陆失败时
	 * @param request
     * @param response
     * @param message 消息包装类
	 * */
	void onLoginFail(HttpServletRequest request,HttpServletResponse response,Message message);
	/**
	 * 用户自定义回调函数，用户登出时
	 * */
	void onLogOut(HttpServletRequest request,HttpServletResponse response,UserPrincipalV7 context);
}