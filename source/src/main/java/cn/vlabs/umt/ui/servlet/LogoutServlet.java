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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SessionService service;
	private IAccountService acct;
	private UMTCredential cred;
	private Config config;
	public LogoutServlet() {
		super();
	}
	
	public void init(){
		BeanFactory factory = (BeanFactory) getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		service= (SessionService) factory.getBean("SessionService");
		acct = (IAccountService)factory.getBean("AccountService");
		cred= (UMTCredential)factory.getBean("UMTCredUtil");
		this.config=(Config)factory.getBean("Config");
	}
	public void destroy(){
		acct=null;
		service=null;
	}
	private void removeCookie(HttpServletResponse response,String domain,String path,String name){
		Cookie cookie=new Cookie(name, "");
		if(!CommonUtils.isNull(domain)){
			cookie.setDomain(domain);
		}
		if(!CommonUtils.isNull(path)){
			cookie.setPath(path);
		}
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String appname=request.getParameter("appname");
		boolean needRedirect=CommonUtils.isNull(request.getParameter("needRedirect"));
		if (appname==null){
			appname="umt";
		}
		String sid=request.getParameter("sid");
		if (sid==null){
			sid=request.getSession().getId();
		}
		String username=getUserName(request);
		String idp=(String)request.getSession().getAttribute(Attributes.THIRDPARTY_URL);
		LoginInfo info=UMTContext.getLoginInfo(request.getSession());
		if (username!=null){
			removeCookie(response, "", "/", Attributes.COOKIE_NAME);
			removeCookie(response, Attributes.SSO_FLAG_DOMAIN, "/", Attributes.SSO_FLAG);
			//TODO 这里增加对应用帐户的查询和管理功能。
			log(request, SessionUtils.getUserId(request), appname);
			//TODO 这里可能成为瓶颈，需要修改 建议使用队列提交
			Thread t = new Thread(new DoLogout(appname, username, RequestUtil.getRemoteIP(request), sid));
			t.start();
			request.getSession().invalidate();
		}
		if(!needRedirect){
			return;
		}
		String webServerURL=request.getParameter(Attributes.RETURN_URL);
		if (webServerURL==null){
			webServerURL=RequestUtil.getContextPath(request)+"/index.jsp";
		}
		if(LoginInfo.TYPE_THIRD_PARTY_UAF.equals(info.getPasswordType())){
			idp="?idp="+URLEncoder.encode(idp,"UTF-8");
			request.setAttribute("uafLogOutUrl", config.getStringProp("uaf.logout.url", "")+idp);
			request.setAttribute(Attributes.RETURN_URL, webServerURL);
			request.getRequestDispatcher("/logout.jsp").forward(request, response);
		}else{
			
			response.sendRedirect(webServerURL);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void log(HttpServletRequest request, int uid, String appname){
		acct.logout(appname, null, uid, RequestUtil.getRemoteIP(request), new Date(), request.getHeader("User-Agent"), "");
	}
	private class DoLogout implements Runnable{
		private String username;
		private String appname;
		private String userip;
		private String sid;
		public DoLogout(String appname, String username, String userip, String sid){
			if (appname==null){
				this.appname="umt";
			}
			else{
				this.appname=appname;
			}
			this.username=username;
			this.userip=userip;
			this.sid=sid;
		}
		public void run() {
			service.logout(appname, username,userip, sid);
		}
	}
	
	private String getUserName(HttpServletRequest request){
		User user=SessionUtils.getUser(request);
		String username=null;
		if (user==null){
			Cookie[] cookies=request.getCookies();
			if (cookies!=null){
				for (Cookie cookie:cookies){
					if (Attributes.COOKIE_NAME.equals(cookie.getName())&&StringUtils.isNotEmpty(cookie.getValue())){
					    try {
							username =new String(cred.decrypt(HexUtil.toBytes(cookie.getValue())),"UTF-8");
						} catch (UnsupportedEncodingException e) {
						}
					}
				}
			}
		}else{
			username=user.getCstnetId();
		}
		return username;
	}

}