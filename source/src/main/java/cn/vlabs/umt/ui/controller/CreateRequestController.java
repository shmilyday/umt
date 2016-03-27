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
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.RegistLog;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.account.IRegistLogDAO;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.ShowPageController;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.CreateEmailRequestFormValidator;
import cn.vlabs.umt.validate.formValidator.impl.CreateRequestFormValidator;

@Controller
@RequestMapping("/createRequest.do")
public class CreateRequestController {

	@Autowired
	private UserService userService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private IAccountService logService;
	@Autowired
	private IRegistLogDAO registDAO;
	@Autowired
	private IUserLoginNameService loginNameService;
	/**
	 * 验证用户是否存在，逻辑是先去数据库差，如果数据库米有，就去邮件系统查
	 * */
	@RequestMapping(params="act=usercheck")
	public void usercheck(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		int rtnCode=userService.isUsed(request.getParameter("username"));
		if(!CommonUtils.isNull(request.getParameter("returnCode"))){
			writer.print(rtnCode!=UserService.USER_NAME_UNUSED?UserService.USER_NAME_USED:UserService.USER_NAME_UNUSED);
		}else{
			writer.println(rtnCode==UserService.USER_NAME_UNUSED);
		}
		writer.flush();
		writer.close();
	}
	/**
	 * 注册umt本地用户
	 * */
	@RequestMapping(params="act=save")
	public String save(HttpServletRequest request, HttpServletResponse response,@ModelAttribute CreateRequestForm rform){
		ErrorMsgs msgs=new CreateRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			request.setAttribute("form", rform);
			return "/regist";
		}
		try {
				User user = rform.getUser();
				UserService us = userService;
				if (us.getUserByUmtId(rform.getUsername()) == null) {
					int uid=us.create(user,LoginNameInfo.STATUS_TEMP);
					SessionUtils.setSessionVar(request, "createUser", user);
					int loginNameId = loginNameService.getLoginNameId(uid, user.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
					String webUrl = ServiceFactory.getWebUrl(request);
					userService.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), uid, rform.getUsername(),webUrl, loginNameId);
					request.setAttribute("sendEmail", rform.getUsername());
					request.setAttribute("title","temp.filter.regist.title");
					LoginInfo info=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(rform.getUsername(),rform.getPassword()));
					UMTContext.saveUser(request.getSession(), info);
					logService.log(UMTLog.EVENT_TYPE_LOG_IN, uid,RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
					String showUrl="redirect:/show.do";
					showUrl=RequestUtil.addParam(showUrl, "act", "showFilterActive");
					showUrl=RequestUtil.addParam(showUrl, "oper","regist");
					showUrl=RequestUtil.addParam(showUrl, "type", User.USER_TYPE_UMT);
					showUrl=RequestUtil.addParam(showUrl, "sendEmail", rform.getUsername());
					return showUrl;
				} else {
					request.setAttribute("message", "regist.user.exist");
					return "/regist";
				}
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return "/regist";
		}
	}
	private RegistLog getRegistLog(HttpServletRequest request,String userName){
		RegistLog log=new RegistLog();
		log.setIp(RequestUtil.getRemoteIP(request));
		log.setOccureTime(new Date());
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setUserName(userName);
		return log;
	}
	/**
	 * 去注册邮箱账号
	 * @throws IOException 
	 * */
	@RequestMapping(params="act=saveToEmail")
	public synchronized String saveToEmail(HttpServletRequest request, HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException{
		ErrorMsgs msgs=new CreateEmailRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			request.setAttribute("form", rform);
			return "/regist_email";
		}
		
		RegistLog log= getRegistLog(request,rform.getUsername());
		if(!registDAO.canRegist(log.getIp())){
			response.sendRedirect(ShowPageController.getMessageUrl(request, "today.regist.limit"));
			return null;
		}
		String appName=request.getParameter("appName");
		String loginUrl=request.getParameter("loginUrl");
		request.setAttribute("loginURL", loginUrl);
		request.setAttribute("appName", appName);
		try {
				User user = rform.getUser();
				UserService us = userService;
				ICoreMailClient client=ICoreMailClient.getInstance();
				if (!client.isUserExt(user.getCstnetId())) {
					boolean flag=client.createUser(user.getCstnetId(),user.getTrueName(),user.getPassword());
					if(flag){
						user.setType(User.USER_TYPE_CORE_MAIL);
						user.setPassword(null);
						int uid=us.create(user,LoginNameInfo.STATUS_ACTIVE);
						SessionUtils.setSessionVar(request, "createUser", user);
						tokenService.createToken(uid,Token.OPERATION_ACTIVATION_PRIMARY_EMAIL, user.getUmtId(),null,Token.STATUS_USED);
						if(!CommonUtils.isNull(rform.getTempSecurityEmail())){
							userService.sendActivicationSecurityMail(new UMTContext(request).getLocale(), user.getId(), rform.getTempSecurityEmail(), ServiceFactory.getWebUrl(request));
						}
						LoginInfo info=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(client.formatEmail(rform.getUsername()),rform.getPassword()));
						UMTContext.saveUser(request.getSession(), info);
						logService.log(UMTLog.EVENT_TYPE_LOG_IN, uid,RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
						registDAO.log(log);
						response.sendRedirect(RequestUtil.getContextPath(request));
						return null;
					}else{
						response.sendRedirect(ShowPageController.getMessageUrl(request, "email.service.exception"));
						return null;
					}
				} else {
					request.setAttribute("message", "regist.user.exist");
					return "/regist";
				}
			
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return "/regist";
		}
	}

}