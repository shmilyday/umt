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
package cn.vlabs.umt.ui.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.ShowPageController;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 对密保邮箱的操作
 * @author lvly
 * @since 2013-1-29
 */
@RequestMapping("/user/securityEmail.do")
@Controller
public class SecurityEmailController { 
	public static final String FROM="from";
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	/**
	 * 显示设置邮箱
	 * */
	@RequestMapping
	public String show(HttpServletRequest request, HttpServletResponse response){
		UMTContext context=new UMTContext(request);
		User u=context.getCurrentUMTUser();
		String securityEmail=u.getSecurityEmail();
		String tempSecurityEmail=userService.getTempSecurityEmail(u.getId());
		request.setAttribute(FROM, request.getParameter(FROM));
		if(CommonUtils.isNull(tempSecurityEmail)&&CommonUtils.isNull(securityEmail)){
			return "/user/setSaftyMail";
		}else{
			request.setAttribute("securityEmail", securityEmail);
			request.setAttribute("tempSecurityEmail", tempSecurityEmail);
			return "/user/changeSaftyMail";
		}
	}
	/**
	 * 保存密保邮箱
	 * */
	@RequestMapping(params="act=saveSecurityEmail")
	public String saveSecurityEmail(HttpServletRequest request,HttpServletResponse response)throws IOException{
		String oper=request.getParameter("oper");
		String securityEmail=request.getParameter("securityEmail");
		String password=request.getParameter("password");
		UMTContext context=new UMTContext(request);
		User u=context.getCurrentUMTUser();
		boolean isPass = loginService.passwordRight(new UsernamePasswordCredential(u.getCstnetId(), password));
		if(!isPass){
			String oldSecurityEmail=u.getSecurityEmail();
			String tempSecurityEmail=userService.getTempSecurityEmail(u.getId());
			request.setAttribute("securityEmail", oldSecurityEmail);
			request.setAttribute("tempSecurityEmail", tempSecurityEmail);
			request.setAttribute("password_error", "security.email.password.error");
			return "change".equals(oper)?"/user/changeSaftyMail":"/user/changeSaftyMail";
		}
		SessionUtils.setSessionVar(request, Attributes.USER_TEMP_SECURITY_EMAIL, securityEmail);
		userService.sendActivicationSecurityMail(context.getLocale(),u.getId(), securityEmail, ServiceFactory.getWebUrl(request));
		String title="";
		if("change".equals(oper)){
			title="security.email.update";
		}else if("set".equals(oper)){
			title="security.email.set";
		}
		
		response.sendRedirect(ShowPageController.getSendSuccessUrl(request, securityEmail, "security", title));
		return null;
	}
	
}