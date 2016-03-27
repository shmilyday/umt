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
package cn.vlabs.umt.ui.activation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活各种东西，比如登陆账号，密保邮箱之类
 * @author lvly
 * @since 2013-1-28
 */
@Controller
@RequestMapping("/temp/activation.do")
public class SendActivationController extends ActivationBaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private IUserLoginNameService loginNameService;

	/**
	 * 发送账户激活邮件
	 * */
	@RequestMapping(params="act=sendLoginEmail")
	public String sendLoginEmail(HttpServletRequest request,
			HttpServletResponse response){
		String loginEmail=request.getParameter("loginEmail");
		int userId = SessionUtils.getUserId(request);
		LoginNameInfo nameInfo=loginNameService.getALoginNameInfo(userId, loginEmail);
		String webUrl = ServiceFactory.getWebUrl(request);
		userService.sendActivicationLoginMail(new UMTContext(request).getLocale(),userId,loginEmail,webUrl,false,nameInfo.getId());
		return getSendSuccessUrl(loginEmail, request.getParameter("type"),  "login.email.success");
	}
	/**
	 * 发送账户激活和密保邮箱邮件
	 * */
	@RequestMapping(params="act=sendLoginAndSecurityEmail")
	public String sendLoginAndSecurityEmail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginEmail=request.getParameter("loginEmail");
		int userId = SessionUtils.getUserId(request);
		LoginNameInfo nameInfo=loginNameService.getALoginNameInfo(userId, loginEmail);
		if(nameInfo==null){
			return null;
		}
		String webUrl = ServiceFactory.getWebUrl(request);
		boolean result=userService.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), userId, loginEmail, webUrl, nameInfo.getId());
		if(!result){
			request.setAttribute("message","email.too.refrequently");
			return "/message";
		}
		return getSendSuccessUrl(loginEmail, "login", "login.email.success");
	}
	/**
	 * 发送辅助账户成功邮件
	 * */
	@RequestMapping(params="act=sendSecondaryEmail")
	public String sendSecondaryEmail(HttpServletRequest request,
			HttpServletResponse response){
		String loginNameInfoId=request.getParameter("loginNameInfoId");
		if(CommonUtils.isNull(loginNameInfoId)||!CommonUtils.isNumber(loginNameInfoId)){
			return null;
		}
		LoginNameInfo loginNameInfo=loginNameService.getLoginNameInfoById(Integer.valueOf(loginNameInfoId));
		if(loginNameInfo==null){
			return getRedirectMessageUrl("secondary.deleted");
		}
		String toEmail=CommonUtils.isNull(loginNameInfo.getTmpLoginName())?loginNameInfo.getLoginName():loginNameInfo.getTmpLoginName();
		int userId = SessionUtils.getUserId(request);
		String webUrl = ServiceFactory.getWebUrl(request);
		userService.sendActivicationSecondaryEmail(new UMTContext(request).getLocale(), userId,toEmail, webUrl, Integer.valueOf(loginNameInfoId),!CommonUtils.isNull(loginNameInfo.getTmpLoginName()));
		return  getSendSuccessUrl(toEmail, "secondary", "secondary.email.success");
	}

	/**
	 * 发送密保邮箱激活邮件
	 * */
	@RequestMapping(params="act=sendSecurityEmail")
	public String sendSecurityEmail(HttpServletRequest request,
			HttpServletResponse response){
		String securityEmail=request.getParameter("securityEmail");
		Token token=tokenService.getATokenByUidAndOperation(SessionUtils.getUserId(request), Token.OPERATION_ACTIVATION_SECURITY_EMAIL, Token.STATUS_UNUSED);
		if(token==null||!token.getContent().equals(securityEmail)){
			return getRedirectMessageUrl("common.data.un.equals");
		}
		userService.sendActivicationSecurityMail(new UMTContext(request).getLocale(),SessionUtils.getUserId(request),securityEmail,ServiceFactory.getWebUrl(request));
		return getSendSuccessUrl(securityEmail,"security","security.email.success");
	}
}