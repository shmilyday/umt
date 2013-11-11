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

import cn.vlabs.umt.common.util.CharUtils;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 更改登录名操作,需要登陆
 * 
 * @author lvly
 * @since 2013-3-4
 */
public class LoginNamePrimaryAction extends DispatchAction {
	/**
	 * 验证邮箱是否可用
	 * */
	public ActionForward isPrimaryEmailUsed(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String primaryEmail=request.getParameter("primaryEmail");
		if(CommonUtils.isNull(primaryEmail)){
			return writeToResponse(response, String.valueOf(false));
		}
		if(ServiceFactory.getUserService(request).isUsed(primaryEmail)){
			return writeToResponse(response,String.valueOf(false));
		}
		if(ServiceFactory.getLoginNameService(request).isUsedByMe(SessionUtils.getUserId(request), primaryEmail)){
			return writeToResponse(response,String.valueOf(false));
		}
		return writeToResponse(response,String.valueOf(true));
	}
	private ActionForward writeToResponse(HttpServletResponse response,String json)throws Exception{
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		writer.println(json);
		writer.flush();
		writer.close();
		return null;
	}
	/**
	 * 显示设置主账号
	 * */
	public ActionForward showPrimaryLoginEmailStep1(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("set.primaryEmail.show.step1.confirm");
	}
	/**
	 * 第一步提交，验证账户密码时候正确
	 * */
	public ActionForward sendConfirm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String password = request.getParameter("password");
		String loginName = request.getParameter("loginName");
		LoginInfo loginInfo = ServiceFactory.getLoginService(request).loginAndReturnPasswordType(
				new UsernamePasswordCredential(loginName, password));
		if (loginInfo.getUser() == null) {
			request.setAttribute("loginName_error", "login.password.wrong");
			return mapping.findForward("set.primaryEmail.show.step1.confirm");
		}
		ServiceFactory.getUserService(request).sendComfirmToOldMail(new UMTContext(request).getLocale(),
		SessionUtils.getUserId(request), loginName, ServiceFactory.getWebUrl(request));
		return mapping.findForward("set.primaryEmail.show.step1.send.success");
	}
	/**
	 * 设置主账号，发送激活邮件
	 * */
	public ActionForward savePrimaryEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String primaryEmail=request.getParameter("primaryEmail");
		if (ServiceFactory.getUserService(request).isUsed(primaryEmail)) {
			request.setAttribute("loginName_error", "primaryEmail.used");
			return mapping.findForward("set.primaryEmail.show.step2");
		}
		UMTContext context = new UMTContext(request);
		String oldEmail=context.getCurrentUMTUser().getCstnetId();
		IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(request);
		ServiceFactory.getUserService(request).sendActivicationLoginMail(context.getLocale(),
				SessionUtils.getUserId(request), primaryEmail, ServiceFactory.getWebUrl(request), true,
				loginNameService.getLoginNameId(SessionUtils.getUserId(request), oldEmail, LoginNameInfo.LOGINNAME_TYPE_PRIMARY));
		loginNameService.updateToLoginName(SessionUtils.getUserId(request),
				oldEmail, primaryEmail);
		//把用户的确认token删掉
		ITokenService tokenService = ServiceFactory.getTokenService(request);
		Token token = tokenService.getATokenByUidAndOperation(SessionUtils.getUserId(request),
				Token.OPERATION_COMFIRM_PRIMARY_EMAIL, Token.STATUS_USED);
		if (token != null) {
			tokenService.removeTokenById(token.getId());
		}
		request.setAttribute("newPrimaryEmail", primaryEmail);
		request.setAttribute("newPrimaryEmailShow", CharUtils.hideEmail(primaryEmail));
		return mapping.findForward("set.primaryEmail.show.step3");
	}
}