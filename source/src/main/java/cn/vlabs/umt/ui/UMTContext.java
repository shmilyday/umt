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
package cn.vlabs.umt.ui;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.role.UMTRole;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.User;

public class UMTContext {
	private static final Logger LOGGER=Logger.getLogger(UMTContext.class);
	public UMTContext(HttpServletRequest request) {
		this.request = request;
	}
	public LoginInfo getLoginInfo() {
		LoginInfo info= (LoginInfo) request.getSession().getAttribute(Attributes.LOGIN_INFO);
		return killNull(info);
	}
	public User getCurrentUMTUser() {
		return getLoginInfo().getUser();
	}
	public static boolean isAdminUser(HttpSession session) {
		UMTRole[] roles = (UMTRole[]) session.getAttribute(Attributes.ROLE);
		if (roles != null) {
			for (UMTRole role : roles) {
				if ("admin".equals(role.getName())){
					return true;
				}
			}
		}
		return false;
	}
	private static LoginInfo killNull(LoginInfo info){
		if(info==null){
			info=new LoginInfo();
		}
		return info;
		
	}
	public static LoginInfo getLoginInfo(HttpSession session) {
		if (session != null) {
			return killNull((LoginInfo) session.getAttribute(Attributes.LOGIN_INFO));
		} else {
			return killNull(null);
		}
	}
	public static Collection<User> getAdminUsers() {
		RoleService rs = (RoleService) factory.getBean("RoleService");
		return rs.getRoleMembers("admin");
	}

	public static void saveUser(HttpSession session, LoginInfo info) {
		info=killNull(info);
		if(info.getLoginNameInfo()==null||info.getUser()==null){
			LOGGER.error("can't set null logininfo to session!");
			return;
		}
		session.setAttribute(Attributes.LOGIN_INFO, info);
	}
	public Locale getLocale() {
		return request.getLocale();
	}

	public Enumeration<Locale> getLocales() {
		return request.getLocales();
	}

	public String getSiteURL() {
		String url = request.getRequestURL().toString();
		int index = url.indexOf('/', "http://".length());
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url + request.getContextPath();
	}

	private HttpServletRequest request;

	public static BeanFactory getFactory() {
		return factory;
	}
	public static void saveRoles(HttpSession session, UMTRole[] roles) {
		session.setAttribute(Attributes.ROLE, roles);
	}

	public static void setFactory(BeanFactory factory) {
		UMTContext.factory = factory;
	}

	private static BeanFactory factory;
}