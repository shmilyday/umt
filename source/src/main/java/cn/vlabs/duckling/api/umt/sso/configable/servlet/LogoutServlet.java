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
package cn.vlabs.duckling.api.umt.sso.configable.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.UserPrincipalV7;
import cn.vlabs.duckling.api.umt.sso.SSOProperties;
import cn.vlabs.duckling.api.umt.sso.SessionUtil;
import cn.vlabs.duckling.api.umt.sso.configable.service.ILoginHandle;
import cn.vlabs.duckling.common.util.ClassUtil;

/**
 * 登出逻辑，于原来相同
 * 
 * @date 2013-2-5
 * @author LvLongYun
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = -198729384L;
	/**
	 * HTTP协议端口号
	 * */
	private static final int HTTP_DEFAULT_PORT=80;
	/**
	 * HTTPS协议端口号
	 * */
	private static final int HTTPS_DEFAULT_PORT=443;
	/**
	 * 默认字符集
	 * */
	private static final String DEFAULT_CHARSET="UTF-8";
	
	private static final Logger LOGGER=Logger.getLogger(LogoutServlet.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String defaultRetoLocalAppUrl = SSOProperties.getInstance()
				.getProperty(ILoginHandle.LOCALAPP_LOGIN_RETURN);
		if (StringUtils.isBlank(defaultRetoLocalAppUrl)) {
			defaultRetoLocalAppUrl = getRootUrlWithContextPath(request);
		}
		String umtSsoLogout = request.getParameter("umtSsoLogout");
		UserPrincipalV7 context = (UserPrincipalV7)SessionUtil.getObj(request,SessionUtil.USER_CONTEXT );
		if (context != null) {
			ILoginHandle handle = getHandle();
			handle.onLogOut(request, response, context);
			if (umtSsoLogout == null) {
				String sid = request.getSession().getId();
				String logoutURL = buildLogoutURL(defaultRetoLocalAppUrl, sid);
				response.sendRedirect(logoutURL);
			}
		} else {
			if (umtSsoLogout == null) {
				response.sendRedirect(defaultRetoLocalAppUrl);
			}
		}
		HttpSession session = request.getSession(false);
		if (session != null){
			session.invalidate();
		}
	}
	private String buildLogoutURL(
			String defaultRetoLocalAppUrl, String sid) {
		String url;
		try {
			url = SSOProperties.getInstance().getProperty(
					ILoginHandle.UMT_LOGOUT_URL_KEY)
					+ "?appname="
					+ URLEncoder.encode(SSOProperties.getInstance()
							.getProperty(ILoginHandle.UMT_AUTH_APPNAME_KEY),
							DEFAULT_CHARSET)
					+ "&sid="
					+ sid
					+ "&WebServerURL="
					+ URLEncoder.encode(defaultRetoLocalAppUrl, DEFAULT_CHARSET);
			return url;
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}
    /**
     * 获得用户自定义，扩展接口
     * */
    private ILoginHandle getHandle(){
    	String loginHandClass = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_LOGIN_EXTHANDLE_CLASS,ILoginHandle.DEFAULT_IMPL_PATH);
    	return (ILoginHandle)ClassUtil.classInstance(loginHandClass);
    } 
    /**
	 * 获得url，带contextPath
	 * @param request http请求
	 * */
	private String getRootUrlWithContextPath(HttpServletRequest request){
		String url=getRootUrlWithOutContextPath(request);
		String contextPath=request.getContextPath();
		if(!StringUtils.isBlank(contextPath)){
			url+=contextPath;
		}
		return url;
	}
	/**
	 * 获得url，不带contextPath
	 * @param request http请求
	 * */
	private String getRootUrlWithOutContextPath(HttpServletRequest request){
		String url = request.getScheme() + "://" + request.getServerName();
		int port = request.getServerPort();
		if ((port != HTTP_DEFAULT_PORT) && (port != HTTPS_DEFAULT_PORT)) {
			url = url + ":" + port;
		}
		return url;
	}
    
}