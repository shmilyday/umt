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
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.ChangePasswordFormValidator;

public class PasswordAction extends DispatchAction {
	public ActionForward showChangePassword(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String returnUrl = request.getHeader("Referer");
		if (request.getParameter("returnUrl") != null) {
			returnUrl = request.getParameter("returnUrl");
		}
		if (returnUrl != null && returnUrl.trim().length() > 0) {
			SessionUtils.setSessionVar(request,"returnUrl", returnUrl);
		}
		String passwordType=new UMTContext(request).getLoginInfo().getPasswordType();
		if(LoginInfo.TYPE_THIRD_PARTY_QQ.equals(passwordType)||LoginInfo.TYPE_THIRD_PARTY_SINA.equals(passwordType)){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "change.password.weibo.error"));
		}
		request.setAttribute("email",SessionUtils.getUser(request).getCstnetId());
		return mapping.findForward("showChangePassword");
	}

	public ActionForward saveChangePassword(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ErrorMsgs msgs=new ChangePasswordFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return toErrorPage(mapping, request);
		}
		String oldpassword = request.getParameter("oldpassword");
		oldpassword = CommonUtils.killNull(oldpassword);
		String password = request.getParameter("password");
		UserService us = ServiceFactory.getUserService(request);
		LoginService lservice = ServiceFactory.getLoginService(request);
		User user=us.getUserByUid(SessionUtils.getUserId(request));
		boolean passwordRight=false;
		if(user.isCoreMailOrUc()){
			passwordRight=lservice.coreMailPasswordRight(new UsernamePasswordCredential(user.getCstnetId(), oldpassword));
			if(passwordRight){
				us.updateCoreMailPassword(user.getCstnetId(), password);
			}
		}else{
			passwordRight=lservice.umtPasswrdRight(new UsernamePasswordCredential(user.getCstnetId(),oldpassword));
			if(passwordRight){
				us.updatePassword(SessionUtils.getUserId(request), password);
			}
		}
		if(!passwordRight){
			request.setAttribute("oldpasswordError", "true");
			return toErrorPage(mapping,request);
		}
		String returnurl = (String) request.getSession().getAttribute(
				"returnUrl");
		if (returnurl == null) {
			returnurl = RequestUtil.getContextPath(request)+"/index.jsp";
		} else {
			request.getSession().removeAttribute("returnUrl");
		}
		getLogService().log(UMTLog.EVENT_TYPE_CHANGE_PASSWORD,SessionUtils.getUserId(request), RequestUtil.getRemoteIP(request), RequestUtil.getBrowseType(request));
		response.sendRedirect(RequestUtil.addParam(returnurl, "msg", "change.password.success"));
		return null;
	}
	private IAccountService getLogService(){
		return (IAccountService)getBeanFactory().getBean(IAccountService.BEAN_ID);
	}
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	private ActionForward toErrorPage(ActionMapping mapping,
			 HttpServletRequest request){
		request.setAttribute("email",new UMTContext(request).getLoginInfo().getUser().getCstnetId());
		return mapping.findForward("showChangePassword");
	}
}