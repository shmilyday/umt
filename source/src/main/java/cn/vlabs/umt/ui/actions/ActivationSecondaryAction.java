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
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.AbstractDoActivation;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.impl.DoActivationServiceForSecondary;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活辅助账号
 * @author lvly
 * @since 2013-3-6
 */
public class ActivationSecondaryAction extends DispatchAction{
	/**
	 * 激活辅助邮箱
	 * */
	public synchronized ActionForward activeSecondaryEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActivationForm data=(ActivationForm)form;
		boolean isValid=getTokenService().isValid(data.getTokenid(), data.getRandom(),Token.OPERATION_ACTIVATION_SECONDARY_EMAIL);
		//token无效
		if(!isValid){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "active.login.email.fail"));
			return null;
		}
		Token token=getTokenService().getTokenById(data.getTokenid());
		User user=getUserService().getUserByUid(token.getUid());
		AbstractDoActivation doAction=new DoActivationServiceForSecondary(request, response, token, user, data);
		return doAction.doActivation();
	}
	/**
	 * 做登陆操作
	 * */
	public ActionForward doLoginSecondary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("token", form);
		request.setAttribute("_primaryEmail",request.getParameter("primaryEmail"));
		return mapping.findForward("set.secondaryEmail.show.login");
	}
	/**
	 * 做密码是否正确操作
	 * */
	public ActionForward validPasswordSecondary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("password");
		LoginInfo loginInfo=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(new UsernamePasswordCredential(loginName, password));
		if(loginInfo.getUser()!=null){
			UMTContext.saveUser(request.getSession(),loginInfo);
			return activeSecondaryEmail(mapping,form,request,response);
		}else{
			request.setAttribute("password_error", "security.email.password.error");
			request.setAttribute("token", form);
			request.setAttribute("_primaryEmail", loginName);
			return mapping.findForward("set.secondaryEmail.show.login");
		}
	}
	private ITokenService getTokenService(){
		return (ITokenService) getBeanFactory().getBean(ITokenService.BEAN_ID);
	}
	private UserService getUserService(){
		return (UserService)getBeanFactory().getBean(UserService.BEAN_ID);
	}
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}

}