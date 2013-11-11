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

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.AbstractDoActivation;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.service.impl.DoActivationServiceForPrimary;
import cn.vlabs.umt.services.user.service.impl.DoActivationServiceForPrimaryAndSecurity;
import cn.vlabs.umt.services.user.service.impl.DoActivationServiceForPrimaryConfirm;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活主账号
 * @author lvly
 * @since 2013-1-28
 */
public class ActivationPrimaryAction extends DispatchAction{
	
	/**
	 * 激活登陆邮箱和密保邮箱，同时进行
	 * */
	public ActionForward activeLoginEmailAndSecurity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActivationForm data=(ActivationForm)form;
		boolean isValid=getTokenService().isValid(data.getTokenid(), data.getRandom(),Token.OPERATION_ACTIVATION_PRIMARY_AND_SECURITY);
		if(isValid){
			Token token=getTokenService().getTokenById(data.getTokenid());
			SessionUtils.setSessionVar(request, Attributes.IS_USER_LOGIN_ACTIVE, true);
			IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(request);
			LoginNameInfo nameInfo=loginNameService.getLoginNameInfoById(data.getLoginNameInfoId());
			if(nameInfo==null){
				response.sendRedirect(ShowPageAction.getMessageUrl(request, "active.login.email.fail"));
				return null;
			}
			User user=getUserService().getUserByUid(token.getUid());
			AbstractDoActivation doAction=new DoActivationServiceForPrimaryAndSecurity(request, response, token, user, data, nameInfo);
			return doAction.doActivation();
		}
		response.sendRedirect(ShowPageAction.getMessageUrl(request,  "active.login.email.fail"));
		return null;
	}
	/**
	 * 激活登陆邮箱
	 **/
	public ActionForward activeLoginEmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		ActivationForm data=(ActivationForm)form;
		boolean isValid=getTokenService().isValid(data.getTokenid(), data.getRandom(),Token.OPERATION_ACTIVATION_PRIMARY_EMAIL);
		if(isValid){
			Token token=getTokenService().getTokenById(data.getTokenid());
			SessionUtils.setSessionVar(request, Attributes.IS_USER_LOGIN_ACTIVE, true);
			IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(request);
			LoginNameInfo nameInfo=loginNameService.getLoginNameInfoById(data.getLoginNameInfoId());
			if(nameInfo==null){
				response.sendRedirect(ShowPageAction.getMessageUrl(request, "active.login.email.fail"));
				return null;
			}
			User user=getUserService().getUserByUid(token.getUid());
			AbstractDoActivation doActivation=new DoActivationServiceForPrimary(request, response, token, user, data, nameInfo, mapping);
			return doActivation.doActivation();
			
			
		}
		response.sendRedirect(ShowPageAction.getMessageUrl(request, (isValid?"active.login.email.success":"active.login.email.fail")));
		return null;
	}
	/**
	 * 确认更改主账户操作，以便接着更改
	 * */
	public ActionForward confirmChangeLoginEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ActivationForm data=(ActivationForm)form;
		boolean isValid=getTokenService().isValid(data.getTokenid(), data.getRandom(),Token.OPERATION_COMFIRM_PRIMARY_EMAIL);
		//token无效
		if(!isValid){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "active.login.email.fail"));
			return null;
		}
		Token token=getTokenService().getTokenById(data.getTokenid());
		User user=getUserService().getUserByUid(token.getUid());
		getTokenService().toUsed(token.getId());
		AbstractDoActivation doActivation=new DoActivationServiceForPrimaryConfirm(request, response, token, user, data, mapping);
		return doActivation.doActivation();
		
	}
	/**
	 * 更改主账户第一步，输入密码，登录
	 * */
	public ActionForward doLoginPrimary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Token token=ServiceFactory.getTokenService(request).getATokenByUidAndOperationWithExpire(SessionUtils.getUserId(request), Token.OPERATION_COMFIRM_PRIMARY_EMAIL, Token.STATUS_USED);
		if(token!=null){
			request.setAttribute("_primaryEmail", request.getParameter("primaryEmail"));
			return mapping.findForward("set.primaryEmail.show.step1.login");
		}else{
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "active.login.email.fail"));
			return null;
		}
	}
	public ActionForward doLoginPrimaryOnlyActive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("_primaryEmail", request.getParameter("primaryEmail"));
		request.setAttribute("_requestAct", request.getParameter("requestAct"));
		request.setAttribute("token",form);
		return mapping.findForward("set.primaryEmail.show.active.login");
	}
	/**
	 * 验证密码是否正确，准备跳入设置主邮箱页面
	 * */
	public ActionForward validPasswordPrimary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String primaryEmail = request.getParameter("loginName");
		String password = request.getParameter("password");
		String requestAct=request.getParameter("requestAct");
		LoginInfo loginInfo=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(
				new UsernamePasswordCredential(primaryEmail, password));
		if (loginInfo.getUser()!=null){
			UMTContext.saveUser(request.getSession(),loginInfo);
			if("activeLogin".equals(requestAct)){
				return activeLoginEmail(mapping, form, request, response);
			}else if("activeLoginAndSecurity".equals(requestAct)){
				return activeLoginEmailAndSecurity(mapping, form, request, response);
			}else{
				return mapping.findForward("set.primaryEmail.show.step2");
			}
		}else{
			request.setAttribute("_primaryEmail", primaryEmail);
			request.setAttribute("password_error", "security.email.password.error");
			if(CommonUtils.isNull(requestAct)){
				return mapping.findForward("set.primaryEmail.show.step1.login");
			}else{
				request.setAttribute("_requestAct", requestAct);
				request.setAttribute("_primaryEmail", primaryEmail);
				request.setAttribute("token",form);
				return mapping.findForward("set.primaryEmail.show.active.login");
			}
		}
	}
	//最终提交，生效
	public ActionForward finalComfirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginName=request.getParameter("loginName");
		String primaryEmail=request.getParameter("primaryEmail");
		String password=request.getParameter("password");
		LoginInfo loginInfo=ServiceFactory.getLoginService(request).loginAndReturnPasswordType(
				new UsernamePasswordCredential(loginName, password));
		if(loginInfo.getUser()!=null){
			UMTContext.saveUser(request.getSession(),loginInfo);
			if(getUserService().isUsed(primaryEmail)){
				response.sendRedirect(ShowPageAction.getMessageUrl(request, "email is qianged"));
				return null;
			}
			int uid=SessionUtils.getUserId(request);
			getUserLoginNameService().updateLoginName(uid,loginName, primaryEmail);
			getUserLoginNameService().updateToLoginName(uid, primaryEmail, null);
			getUserLoginNameService().toActive(uid, loginName, LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
			getLogService().log(UMTLog.EVENT_TYPE_CHANGE_LOGIN_NAME, uid, RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
			getUserService().updateValueByColumn(SessionUtils.getUserId(request), "cstnet_id", primaryEmail);
			request.setAttribute("email", primaryEmail);
			return mapping.findForward("set.primaryEmail.show.step4");
		}else{
			request.setAttribute("_oldEmail", loginName);
			request.setAttribute("_newPrimary",primaryEmail);
			request.setAttribute("password_error", "security.email.password.error");
			return mapping.findForward("set.primaryEmail.show.step3.check");
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
	private IAccountService getLogService(){
		return (IAccountService)getBeanFactory().getBean(IAccountService.BEAN_ID);
	}
	private IUserLoginNameService getUserLoginNameService(){
		return(IUserLoginNameService)getBeanFactory().getBean(IUserLoginNameService.BEAN_ID);
	}
}