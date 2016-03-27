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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.exception.UserNotFound;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.tags.utils.EmailUrlTagUtils;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.RemindPasswordFormValidator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;
/**
 * @author lvly
 * @since 2013-1-30
 */
@Controller
@RequestMapping("/findPsw.do")
public class FindPasswordController {
	@Autowired
	private UserService userService;
	/**
	 * 显示第一步
	 **/
	@RequestMapping(params = "act=stepOne")
	public String stepOne(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("isStatic", "true");
		request.setAttribute("loginEmail",
				request.getParameter("loginEmail"));
		return "/findPswGuide";
	}
	/**
	 * 验证第一步，并跳到第二步
	 * */
	@RequestMapping(params = "act=submitStepOne")
	public String submitStepOne(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("isStatic", "true");
		ErrorMsgs msgs = new RemindPasswordFormValidator(request)
				.validateForm();
		if (!msgs.isPass()) {
			request.setAttribute("loginEmail",
					request.getParameter("loginEmail"));
			return "/findPswGuide";
		}
		String loginEmail = request.getParameter("loginEmail");
		User user = userService.getUserByLoginName(loginEmail);
		SessionUtils.setSessionVar(request, "findPswUserId", user.getId());
		return "redirect:/findPsw.do?act=stepTwo";
	}
	@RequestMapping(params = "act=stepTwo")
	public String stepTwo(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("isStatic", "true");
		String uidStr = SessionUtils.getSessionVar(request, "findPswUserId");
		if (CommonUtils.isNull(uidStr)) {
			return stepOne(request, response);
		}
		int uid = Integer.valueOf(uidStr);
		User user = userService.getUserByUid(uid);
		if (user == null
				|| (CommonUtils.isNull(user.getSecurityEmail())
						&& CommonUtils.isNull(user.getSecondaryEmails()) && user
							.isCoreMailOrUc())) {
			request.setAttribute("coreMailOnly", true);
		} else {
			String tempSecurityEmail = userService.getTempSecurityEmail(user
					.getId());
			if (CommonUtils.isNull(user.getSecurityEmail())) {
				user.setSecurityEmail(tempSecurityEmail);
			}
			request.setAttribute("coreMailOnly", false);
		}
		request.setAttribute("findPswUser", user);
		request.setAttribute("active", request.getParameter("active"));
		return "/findPswMail";
	}
	private boolean validateSendEmailCorrect(int uid, String loginEmail) {
		User u = userService.getUserByUid(uid);
		if (u == null) {
			return false;
		}
		if (u.getCstnetId().equals(loginEmail)) {
			return true;
		}
		if (!CommonUtils.isNull(u.getSecondaryEmails())) {
			for (String secondaryEmail : u.getSecondaryEmails()) {
				if (secondaryEmail.equals(loginEmail)) {
					return true;
				}
			}
		}
		if (loginEmail.equals(u.getSecurityEmail())) {
			return true;
		}

		return false;
	}
	/**
	 * 发送找回密码邮箱
	 * 
	 * @throws UserNotFound
	 * 
	 * */
	@RequestMapping(params = "act=sendRemindeEmail")
	public void sendRemindeEmail(HttpServletRequest request,
			HttpServletResponse response) throws IOException, UserNotFound {
		request.setAttribute("isStatic", "true");
		String loginEmail = request.getParameter("loginEmail");
		int uid = Integer.valueOf(request.getParameter("uid"));
		if (loginEmail != null) {
			loginEmail = loginEmail.toLowerCase();
		}
		UserService us = userService;
		boolean flag = false;
		if (!ValidatorFactory.getValidCodeEqualsValidator(request).validate(
				request.getParameter("ValidCode"))) {
			flag = false;
		} else if (validateSendEmailCorrect(uid, loginEmail)) {
			us.sendChangeMail(new UMTContext(request).getLocale(), uid,
					loginEmail, ServiceFactory.getWebUrl(request));
			flag = true;
		}
		PrintWriter writer = response.getWriter();
		writer.println("{\"result\":" + flag + ",\"url\":\""
				+ EmailUrlTagUtils.getEmailLink(loginEmail) + "\"}");
		writer.flush();
		writer.close();
	}

}