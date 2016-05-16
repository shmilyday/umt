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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.ShowPageController;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 更改登录名操作,需要登陆
 * 
 * @author lvly
 * @since 2013-3-4
 */
@Controller
@RequestMapping("/user/secondary/loginName.do")
public class SecondaryLoginNameController {
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;

	/**
	 * 显示设置辅助账号界面
	 * */
	@RequestMapping(params = "act=setSecondaryEmail")
	public String setSecondaryEmail(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/secondary/login_name_set_secondary_show";
	}

	/**
	 * 设置辅助账号
	 * 
	 * @throws IOException
	 **/
	@RequestMapping(params = "act=addSecondaryEmail")
	public String addSecondaryEmail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String primaryEmail = request.getParameter("loginName");
		String password = request.getParameter("password");
		String newSecondaryEmail = request.getParameter("newSecondaryEmail");
		if (loginService.passwordRight(new UsernamePasswordCredential(
				primaryEmail, password))) {
			int userId = SessionUtils.getUserId(request);
			if (!loginNameService.isUsedByMe(userId, newSecondaryEmail)) {
				int loginNameId = loginNameService.createLoginName(
						newSecondaryEmail, userId,
						LoginNameInfo.LOGINNAME_TYPE_SECONDARY,
						LoginNameInfo.STATUS_TEMP);
				userService.sendActivicationSecondaryEmail(new UMTContext(
						request).getLocale(), userId, newSecondaryEmail,
						ServiceFactory.getWebUrl(request), loginNameId, false);
			}
			request.setAttribute("email", newSecondaryEmail);

		} else {
			request.setAttribute("newSecondaryEmail", newSecondaryEmail);
			request.setAttribute("password_error",
					"security.email.password.error");
			return "/user/secondary/login_name_set_secondary_show";
		}
		response.sendRedirect(ShowPageController.getSendSuccessUrl(request,
				newSecondaryEmail, "secondary",
				"accountManage.secondaryMail.add"));
		return null;
	}
	/**
	 * 删除辅助账号，已激活的
	 * */
	@RequestMapping(params = "act=deleteSecondary")
	public String deleteSecondary(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String loginNameInfoId = request.getParameter("loginNameInfoId");
		String from = request.getParameter("from");
		if (!CommonUtils.isNull(loginNameInfoId)
				&& CommonUtils.isNumber(loginNameInfoId)
				&& !CommonUtils.isNull(from)) {
			loginNameService.removeLoginNameById(Integer
					.valueOf(loginNameInfoId));
			int userId = SessionUtils.getUserId(request);
			userService.updateValueByColumn(userId, "secondary_email",
					loginNameService.getValidSecondaryEmailStr(userId));
			if ("index".equals(from)) {
				return "redirect:/index.jsp";
			} else {
				return "redirect:/user/manage.do?act=showManage";
			}
		} else {
			response.sendRedirect(ShowPageController.getMessageUrl(request,
					"delete.error"));
			return null;
		}
	}

	/**
	 * 显示修改辅助油箱，已激活
	 * */
	@RequestMapping(params = "act=updateSecondary")
	public String updateSecondary(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("loginNameInfoId",
				request.getParameter("loginNameInfoId"));
		request.setAttribute("email", request.getParameter("email"));
		request.setAttribute("newSecondaryEmail",
				request.getParameter("newSecondaryEmail"));
		return "/user/secondary/login_name_set_secondary_update";
	}

	/**
	 * 修改辅助邮箱
	 * 
	 * @throws IOException
	 * */
	@RequestMapping(params = "act=saveSecondary")
	public String saveSecondary(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		String newSecondaryEmail = request.getParameter("newSecondaryEmail");
		if (!loginService.passwordRight(new UsernamePasswordCredential(
				loginName, password))) {
			request.setAttribute("password_error",
					"security.email.password.error");
			request.setAttribute("newSecondary", newSecondaryEmail);
			return updateSecondary(request, response);
		}

		String loginNameInfoId = request.getParameter("loginNameInfoId");
		LoginNameInfo loginNameInfo = loginNameService
				.getLoginNameInfoById(Integer.valueOf(loginNameInfoId));
		if (loginNameInfo == null) {
			response.sendRedirect(ShowPageController.getMessageUrl(request,
					"secondary.deleted"));
			return null;
		}
		int userId = SessionUtils.getUserId(request);
		if (loginNameInfo.getStatus().equals(LoginNameInfo.STATUS_ACTIVE)) {
			loginNameService.updateToLoginName(userId,
					loginNameInfo.getLoginName(), newSecondaryEmail);
		} else {
			loginNameService.updateLoginName(userId,
					loginNameInfo.getLoginName(), newSecondaryEmail);
		}
		userService.sendActivicationSecondaryEmail(
				new UMTContext(request).getLocale(), userId, newSecondaryEmail,
				ServiceFactory.getWebUrl(request), loginNameInfo.getId(),
				loginNameInfo.getStatus().equals(LoginNameInfo.STATUS_ACTIVE));
		response.sendRedirect(ShowPageController.getSendSuccessUrl(request,
				newSecondaryEmail, "secondary",
				"accountManage.secondaryMail.update"));
		return null;
	}

}