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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.MessagePage;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.UpdatePasswordFormValidator;

public class UpdatePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = request.getParameter("act");
		if ("cancel".equals(act)){
			doCancel(request, response);
		}else if ("updatepass".equals(act)){
			doUpdate(request, response);
		}else{
			doShowJSP(request, response);
		}
	}
	
	
	private void doUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ErrorMsgs msgs=new UpdatePasswordFormValidator(request).validateForm();
		Token token= (Token) request.getSession().getAttribute("TokenObject");
		if(!msgs.isPass()){
			request.setAttribute("username", service.getUserByUid(token.getUid()).getCstnetId());
			forward("/inputpassword.jsp", request, response);
			return;
		}
		if (token==null){
			MessagePage.showNoMenuPage("remindpass.invalid.token", request, response);
			return;
		}
		String password = request.getParameter("password");
		User user=service.getUserByUid(token.getUid());
		if(user.isCoreMailOrUc()){
			service.updateCoreMailPassword(user.getCstnetId(), password);
		}else{
			service.updatePassword(token.getUid(), password);
		}
		request.getSession(true).invalidate();
		LoginInfo info=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(new UsernamePasswordCredential(user.getCstnetId(),password));
		UMTContext.saveUser(request.getSession(true), info);
		tokenService.toUsed(token.getId());
		logService.log(UMTLog.EVENT_TYPE_CHANGE_PASSWORD, user.getId(), RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
		request.getSession().removeAttribute("TokenObject");
		MessagePage.showNoMenuPage("remindpass.update.success", request, response);
	}


	private void doShowJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tokenidString=request.getParameter("tokenid");
		String random = request.getParameter("random");
		request.setAttribute("isStatic", "true");
		try{
			int tokenid = Integer.parseInt(tokenidString);
			if (tokenService.isValid(tokenid, random,Token.OPERATION_CHANGE_PASSWORD)){
				Token token = tokenService.getTokenById(tokenid);
				request.getSession().setAttribute("TokenObject", token);
				request.setAttribute("username", service.getUserByUid(token.getUid()).getCstnetId());
				forward("/inputpassword.jsp", request, response);
			}else{
				MessagePage.showNoMenuPage("remindpass.invalid.token", request, response);
			}
		}catch (NumberFormatException e){
			MessagePage.showNoMenuPage("remindpass.invalid.token", request, response);
		}
	}


	private void doCancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tokenidString=request.getParameter("tokenid");
		String random=request.getParameter("random");
		try{
			int tokenid=Integer.parseInt(tokenidString);
			if (tokenService.isValid(tokenid, random,Token.OPERATION_CHANGE_PASSWORD)){
				service.removeToken(tokenid);
				MessagePage.showNoMenuPage( "remindpass.cancel.success", request, response);
			}else{
				MessagePage.showNoMenuPage("remindpass.invalid.token", request, response);
			}
		}catch (NumberFormatException e){
			MessagePage.showNoMenuPage("remindpass.invalid.token", request, response);
		}
	}
	
	private void forward(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void init() throws ServletException {
		BeanFactory factory = (BeanFactory)getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		service =(UserService) factory.getBean("UserService");
		tokenService=(ITokenService) factory.getBean(ITokenService.BEAN_ID);
		logService=(IAccountService)factory.getBean(IAccountService.BEAN_ID);
	}
	private UserService service;
	
	private ITokenService tokenService;
	private IAccountService logService;
}