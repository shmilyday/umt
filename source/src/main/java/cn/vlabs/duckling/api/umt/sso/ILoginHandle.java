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
package cn.vlabs.duckling.api.umt.sso;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Introduction Here.
 * @date 2010-7-5
 * @author Fred Zhang (fred@cnic.cn)
 */
public interface ILoginHandle {
	public static final String UMT_LOGIN_URL_KEY = "umt.login.url"; 
	public static final String UMT_LOGOUT_URL_KEY = "umt.logout.url"; 
	public static final String UMT_PUBLICKEY_URL_KEY = "umt.publicKey.url"; 
	public static final String LOCALAPP_LOGOUT_URL = "localapp.logout.url"; 
	public static final String LOCALAPP_LOGIN_RETURN = "localapp.login.return"; 
	public static final String UMT_AUTH_APPNAME_KEY = "umt.auth.appname";
	public static final String UMT_AUTH_RULE_KEY = "umt.auth.IgnoreFile";
	public static final String UMT_LOGIN_EXTHANDLE_CLASS = "umt.login.exthandle.class";
	public static final String APP_REGISTER_URL_KEY = "registerUrl";
	/**
	 * 用户登陆之前，系统调用此方法<br>
	 * 可以在此方法中设置一些参数传到umt
	 * @param request
	 * @param response
	 * @param extParamsToUmt
	 */
	void initBeforLogin(HttpServletRequest request, HttpServletResponse response,Map<String,String> extParamsToUmt);
    /**
     * 用户登录成功后，初始化UserContext的额外信息<br>
     * 例如：<br>
     * 添加用户的principals
     * @param request
     * @param response
     * @param context
     */
	void initAfterLogin(HttpServletRequest request, HttpServletResponse response,UserContext context);
    /**
     * 用户退出之前，系统调用此方法
     * @param request
     * @param response
     * @param context
     */
	void destroyBeforeLogout(HttpServletRequest request,HttpServletResponse response,UserContext context);
	
}