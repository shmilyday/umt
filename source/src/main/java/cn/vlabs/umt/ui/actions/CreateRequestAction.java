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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.CreateEmailRequestFormValidator;
import cn.vlabs.umt.validate.formValidator.impl.CreateRequestFormValidator;

/**
 * MyEclipse Struts Creation date: 12-28-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/createRequest" name="createRequestForm"
 *                input="/regist.jsp" scope="request" validate="true"
 */
public class CreateRequestAction extends DispatchAction {
	/**
	 * 验证用户是否存在，逻辑是先去数据库差，如果数据库米有，就去邮件系统查
	 * */
	public ActionForward usercheck(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		writer.println(!getUserService().isUsed(request.getParameter("username")));
		writer.flush();
		writer.close();
		return null;
	}
	/**
	 * 注册umt本地用户
	 * */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		ErrorMsgs msgs=new CreateRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return mapping.getInputForward();
		}
		CreateRequestForm rform = (CreateRequestForm) form;
		try {
			if (checkCode(rform, request)) {
				User user = rform.getUser();
				UserService us = getUserService();
				if (us.getUserByUmtId(rform.getUsername()) == null) {
					int uid=us.create(user,LoginNameInfo.STATUS_TEMP);
					SessionUtils.setSessionVar(request, "createUser", user);
					getUserService().sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), uid, rform.getUsername(),ServiceFactory.getWebUrl(request), ServiceFactory.getLoginNameService(request).getLoginNameId(uid, user.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY));
					request.setAttribute("sendEmail", rform.getUsername());
					request.setAttribute("title","temp.filter.regist.title");
					LoginInfo info=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(new UsernamePasswordCredential(rform.getUsername(),rform.getPassword()));
					UMTContext.saveUser(request.getSession(), info);
					ServiceFactory.getLogService(request).log(UMTLog.EVENT_TYPE_LOG_IN, uid,RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
					String showUrl=RequestUtil.getContextPath(request)+"/show.do";
					showUrl=RequestUtil.addParam(showUrl, "act", "showFilterActive");
					showUrl=RequestUtil.addParam(showUrl, "oper","regist");
					showUrl=RequestUtil.addParam(showUrl, "type", User.USER_TYPE_UMT);
					showUrl=RequestUtil.addParam(showUrl, "sendEmail", rform.getUsername());
					response.sendRedirect(showUrl);
					return null;
				} else {
					request.setAttribute("message", "regist.user.exist");
					return mapping.getInputForward();
				}
			} else {
				rform.setValidcode("");
				request.setAttribute("message", "regist.wrongValidcode");
				return mapping.getInputForward();
			}
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return mapping.getInputForward();
		}
	}
	/**
	 * 去注册邮箱账号
	 * */
	public synchronized ActionForward saveToEmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		ErrorMsgs msgs=new CreateEmailRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return mapping.findForward("email.input.error");
		}
		CreateRequestForm rform = (CreateRequestForm) form;
		String inputForWard="email.input.error";
		String appName=request.getParameter("appName");
		String loginUrl=request.getParameter("loginUrl");
		request.setAttribute("loginURL", loginUrl);
		request.setAttribute("appName", appName);
		try {
			if (checkCode(rform, request)) {
				User user = rform.getUser();
				UserService us = getUserService();
				ICoreMailClient client=ICoreMailClient.getInstance();
				if (!client.isUserExt(user.getCstnetId())) {
					boolean flag=client.createUser(user.getCstnetId(),user.getTrueName(),user.getPassword());
					if(flag){
						user.setType(User.USER_TYPE_CORE_MAIL);
						user.setPassword(null);
						int uid=us.create(user,LoginNameInfo.STATUS_ACTIVE);
						SessionUtils.setSessionVar(request, "createUser", user);
						getTokenService().createToken(uid,Token.OPERATION_ACTIVATION_PRIMARY_EMAIL, user.getUmtId(),null,Token.STATUS_USED);
						if(!CommonUtils.isNull(rform.getTempSecurityEmail())){
							getUserService().sendActivicationSecurityMail(new UMTContext(request).getLocale(), user.getId(), rform.getTempSecurityEmail(), ServiceFactory.getWebUrl(request));
						}
						LoginInfo info=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(new UsernamePasswordCredential(client.formatEmail(rform.getUsername()),rform.getPassword()));
						UMTContext.saveUser(request.getSession(), info);
						ServiceFactory.getLogService(request).log(UMTLog.EVENT_TYPE_LOG_IN, uid,RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
						response.sendRedirect(RequestUtil.getContextPath(request));
						return null;
					}else{
						response.sendRedirect(ShowPageAction.getMessageUrl(request, "email.service.exception"));
						return null;
					}
				} else {
					request.setAttribute("message", "regist.user.exist");
					return mapping.findForward(inputForWard);
				}
			} else {
				rform.setValidcode("");
				request.setAttribute("message", "regist.wrongValidcode");
				return mapping.findForward(inputForWard);
			}
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return mapping.findForward(inputForWard);
		}
	}

	private UserService getUserService() {
		return (UserService) getBeanFactory().getBean(UserService.BEAN_ID);
	}
	private ITokenService getTokenService(){
		return (ITokenService)getBeanFactory().getBean(ITokenService.BEAN_ID);
	}
	
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	private boolean checkCode(CreateRequestForm rform,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		String code = (String) session.getAttribute(Attributes.VALID_CODE);
		return (code != null && code.equals(rform.getValidcode()));
	}
}