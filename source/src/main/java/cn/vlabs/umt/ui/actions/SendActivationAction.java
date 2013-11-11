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
package cn.vlabs.umt.ui.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活各种东西，比如登陆账号，密保邮箱之类
 * @author lvly
 * @since 2013-1-28
 */
public class SendActivationAction extends DispatchAction{
	/**
	 * 发送账户激活邮件
	 * */
	public ActionForward sendLoginEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		String loginEmail=request.getParameter("loginEmail");
		LoginNameInfo nameInfo=ServiceFactory.getLoginNameService(request).getALoginNameInfo(SessionUtils.getUserId(request), loginEmail);
		ServiceFactory.getUserService(request).sendActivicationLoginMail(new UMTContext(request).getLocale(),SessionUtils.getUserId(request),loginEmail,ServiceFactory.getWebUrl(request),false,nameInfo.getId());
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request, loginEmail, request.getParameter("type"),  "login.email.success"));
		return null;
	}
	/**
	 * 发送账户激活和密保邮箱邮件
	 * */
	public ActionForward sendLoginAndSecurityEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginEmail=request.getParameter("loginEmail");
		LoginNameInfo nameInfo=ServiceFactory.getLoginNameService(request).getALoginNameInfo(SessionUtils.getUserId(request), loginEmail);
		ServiceFactory.getUserService(request).sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), SessionUtils.getUserId(request), loginEmail, ServiceFactory.getWebUrl(request), nameInfo.getId());
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request, loginEmail, "login", "login.email.success"));
		return null;
	}
	/**
	 * 发送辅助账户成功邮件
	 * */
	public ActionForward sendSecondaryEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		String loginNameInfoId=request.getParameter("loginNameInfoId");
		if(CommonUtils.isNull(loginNameInfoId)||!CommonUtils.isNumber(loginNameInfoId)){
			return null;
		}
		LoginNameInfo loginNameInfo=ServiceFactory.getLoginNameService(request).getLoginNameInfoById(Integer.valueOf(loginNameInfoId));
		if(loginNameInfo==null){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "secondary.deleted"));
			return null;
		}
		String toEmail=loginNameInfo.getTmpLoginName()==null?loginNameInfo.getLoginName():loginNameInfo.getTmpLoginName();
		ServiceFactory.getUserService(request).sendActivicationSecondaryEmail(new UMTContext(request).getLocale(), SessionUtils.getUserId(request),toEmail, ServiceFactory.getWebUrl(request), Integer.valueOf(loginNameInfoId),!CommonUtils.isNull(loginNameInfo.getTmpLoginName()));
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request, toEmail, "secondary", "secondary.email.success"));
		return null;
	}

	/**
	 * 发送密保邮箱激活邮件
	 * */
	public ActionForward sendSecurityEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		String securityEmail=request.getParameter("securityEmail");
		Token token=ServiceFactory.getTokenService(request).getATokenByUidAndOperation(SessionUtils.getUserId(request), Token.OPERATION_ACTIVATION_SECURITY_EMAIL, Token.STATUS_UNUSED);
		if(token==null||!token.getContent().equals(securityEmail)){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		ServiceFactory.getUserService(request).sendActivicationSecurityMail(new UMTContext(request).getLocale(),SessionUtils.getUserId(request),securityEmail,ServiceFactory.getWebUrl(request));
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request,securityEmail,"security","security.email.success"));
		return null;
	}
}