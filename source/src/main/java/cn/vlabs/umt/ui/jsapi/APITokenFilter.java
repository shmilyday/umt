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
package cn.vlabs.umt.ui.jsapi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IOauthTokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * Servlet Filter implementation class APITokenFilter
 */
public class APITokenFilter implements Filter {
	private IOauthTokenService tokenService;
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = request.getParameter("access_token");
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		if (token!=null && !token.equals(getSavedAccessToken(httpRequest))){
			OauthToken authToken=getOAuthTokenService(request).getTokenByAccess(token);
			if (authToken!=null){
				User user = ServiceFactory.getUserService(httpRequest).getUserByUid(Integer.parseInt(authToken.getUid()));
				if (user!=null){
					user.setPassword(null);
					LoginInfo loginInfo = new LoginInfo();
					loginInfo.setUser(user);
					LoginNameInfo loginNameInfo = new LoginNameInfo();
					loginNameInfo.setId(user.getId());
					loginNameInfo.setLoginName(user.getCstnetId());
					loginNameInfo.setStatus(user.getAccountStatus());
					loginNameInfo.setUid(Integer.parseInt(authToken.getUid()));
					loginInfo.setLoginNameInfo(loginNameInfo);
					UMTContext.saveUser(httpRequest.getSession(), loginInfo);
				}
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
	private IOauthTokenService getOAuthTokenService(ServletRequest request){
		if (tokenService==null){
			BeanFactory factory = (BeanFactory)request.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
			tokenService= (IOauthTokenService)factory.getBean(IOauthTokenService.BEAN_ID);
		}
		return tokenService;
	}
	private String getSavedAccessToken(HttpServletRequest request){
		if (request.getSession(false)!=null){
			return (String)request.getSession().getAttribute("access_token");
		}else{
			return null;
		}
	}
}