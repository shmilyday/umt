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
package cn.vlabs.umt.ui.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.user.bean.CookieCredential;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.servlet.login.LoginMethod;
@Controller
@RequestMapping("/js/isLogin.do")
public class IsLoginController {
	/**
	 * 允许IE跨域访问
	 * @param response
	 */
	private void addCrossDomainHeader(HttpServletResponse response){
		response.setHeader(	"P3P","CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
	}
	
	@RequestMapping
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/javascript");
		response.setHeader("Cache-Control", "no-cache") ;
		addCrossDomainHeader(response);
		Writer w = response.getWriter();
		if (isSessionAuthenticated(request) || isPCookieAuthenticated(request)){
			w.write("var data={result:true}");
		}else{
			w.write("var data={result:false}");
		}
		w.flush();
		w.close();
	}
	
	private boolean isPCookieAuthenticated(HttpServletRequest request){
		String cookieValue=LoginMethod.getSsoCookieValue(request);
		LoginInfo info=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(new CookieCredential(cookieValue, RequestUtil.getRemoteIP(request)));
		return info.getUser()!=null;
	}
	
	private boolean isSessionAuthenticated(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if (session!=null){
			return UMTContext.getLoginInfo(session).getUser()!=null;
		}
		return false;
	}
}