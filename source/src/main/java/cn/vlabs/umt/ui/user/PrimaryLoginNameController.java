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
package cn.vlabs.umt.ui.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CharUtils;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
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
@Controller
@RequestMapping("/user/primary/loginName.do")
public class PrimaryLoginNameController {
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private UserService userService;
	/**
	 * 验证邮箱是否可用
	 * */
	@RequestMapping(params = "act=isPrimaryEmailUsed")
	public void isPrimaryEmailUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String primaryEmail = request.getParameter("primaryEmail");
		if (CommonUtils.isNull(primaryEmail)) {
			writeToResponse(response, String.valueOf(false));
		}
		if (userService.isUsed(primaryEmail) != UserService.USER_NAME_UNUSED) {
			writeToResponse(response, String.valueOf(false));
		}
		if (loginNameService.isUsedByMe(SessionUtils.getUserId(request),
				primaryEmail)) {
			writeToResponse(response, String.valueOf(false));
		}
		writeToResponse(response, String.valueOf(true));
	}

	/**
	 * 设置主账号，发送激活邮件
	 * */
	@RequestMapping(params = "act=savePrimaryEmail")
	public String savePrimaryEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String primaryEmail = request.getParameter("primaryEmail");
		if (userService.isUsed(primaryEmail) != UserService.USER_NAME_UNUSED) {
			request.setAttribute("loginName_error", "primaryEmail.used");
			return "/user/primary/login_name_step2";
		}
		UMTContext context = new UMTContext(request);
		String oldEmail = context.getCurrentUMTUser().getCstnetId();
		userService.sendActivicationLoginMail(context.getLocale(), SessionUtils
				.getUserId(request), primaryEmail, ServiceFactory
				.getWebUrl(request), true, loginNameService.getLoginNameId(
				SessionUtils.getUserId(request), oldEmail,
				LoginNameInfo.LOGINNAME_TYPE_PRIMARY));
		loginNameService.updateToLoginName(SessionUtils.getUserId(request),
				oldEmail, primaryEmail);
		Token token = tokenService.getATokenByUidAndOperation(
				SessionUtils.getUserId(request),
				Token.OPERATION_COMFIRM_PRIMARY_EMAIL, Token.STATUS_USED);
		if (token != null) {
			tokenService.removeTokenById(token.getId());
		}
		request.setAttribute("newPrimaryEmail", primaryEmail);
		request.setAttribute("newPrimaryEmailShow",
				CharUtils.hideEmail(primaryEmail));
		return "/user/primary/login_name_step3";
	}
	/**
	 * 第一步提交，验证账户密码时候正确
	 * */
	@RequestMapping(params = "act=sendConfirm")
	public String sendConfirm(HttpServletRequest request,
			HttpServletResponse response) {
		String password = request.getParameter("password");
		String loginName = request.getParameter("loginName");
		LoginInfo loginInfo = loginService
				.loginAndReturnPasswordType(new UsernamePasswordCredential(
						loginName, password));
		if (loginInfo.getUser() == null) {
			request.setAttribute("loginName_error", "login.password.wrong");
			return "/user/primary/login_name_step1_confirm";
		}
		userService.sendComfirmToOldMail(new UMTContext(request).getLocale(),
				SessionUtils.getUserId(request), loginName,
				ServiceFactory.getWebUrl(request));
		return "/user/primary/login_name_step1_send_success";
	}
	/**
	 * 显示设置主账号
	 * */
	@RequestMapping(params = "act=showPrimaryLoginEmailStep1")
	public String showPrimaryLoginEmailStep1(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/primary/login_name_step1_confirm";
	}
	private void writeToResponse(HttpServletResponse response, String json)
			throws IOException {
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		writer.println(json);
		writer.flush();
		writer.close();
	}
}