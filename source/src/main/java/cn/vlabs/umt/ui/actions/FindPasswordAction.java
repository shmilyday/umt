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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.tags.utils.EmailUrlTagUtils;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.RemindPasswordFormValidator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;
/**
 * @author lvly
 * @since 2013-1-30
 */
public class FindPasswordAction extends DispatchAction{
	/**
	 * 显示第一步
	 **/
	public ActionForward stepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("isStatic", "true");
		return mapping.findForward("find.password.stepOne");
	}
	/**
	 * 验证第一步，并跳到第二步
	 * */
	public ActionForward submitStepOne(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("isStatic", "true");
		ErrorMsgs msgs=new RemindPasswordFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return mapping.findForward("find.password.stepOne");
		}
		String loginEmail=request.getParameter("loginEmail");
		User user=getUserService().getUserByLoginName(loginEmail);
		SessionUtils.setSessionVar(request,"findPswUserId", user.getId());
		response.sendRedirect(RequestUtil.getContextPath(request)+"/findPsw.do?act=stepTwo");
		return null;
	}
	public ActionForward stepTwo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("isStatic", "true");
		String uidStr=SessionUtils.getSessionVar(request, "findPswUserId");
		if(CommonUtils.isNull(uidStr)){
			return stepOne(mapping, form, request, response);
		}
		int uid=Integer.valueOf(uidStr);
		User user=getUserService().getUserByUid(uid);
		if(user==null||(CommonUtils.isNull(user.getSecurityEmail())
				&&CommonUtils.isNull(user.getSecondaryEmails())
				&&user.isCoreMailOrUc())){
			request.setAttribute("coreMailOnly", true);
		}else{
			String tempSecurityEmail=getUserService().getTempSecurityEmail(user.getId());
			if(CommonUtils.isNull(user.getSecurityEmail())){
				user.setSecurityEmail(tempSecurityEmail);
			}
			request.setAttribute("coreMailOnly", false);
		}
		request.setAttribute("findPswUser", user);
		request.setAttribute("active", request.getParameter("active"));
		return mapping.findForward("find.password.stepTwo");
	}
	/**
	 * 发送找回密码邮箱
	 * 
	 * */
	public ActionForward sendRemindeEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("isStatic", "true");
		String loginEmail = request.getParameter("loginEmail");
		int uid=Integer.valueOf(request.getParameter("uid"));
		if (loginEmail != null) {
			loginEmail = loginEmail.toLowerCase();
		}
		UserService us = getUserService();
		boolean flag;
		if(!ValidatorFactory.getValidCodeEqualsValidator(request).validate(request.getParameter("ValidCode"))){
			flag=false;
		}else{
			us.sendChangeMail(new UMTContext(request).getLocale(),uid, loginEmail,ServiceFactory.getWebUrl(request));
			flag=true;
		}
		PrintWriter writer = response.getWriter();
		writer.println("{\"result\":"+flag+",\"url\":\""+EmailUrlTagUtils.getEmailLink(loginEmail)+"\"}");
		writer.flush();
		writer.close();
		return null;
	}
	private UserService getUserService() {
		return (UserService) getBeanFactory().getBean(UserService.BEAN_ID);
	}
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}

}