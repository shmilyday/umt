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
package cn.vlabs.umt.ui.activation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活主账号
 * 
 * @author lvly
 * @since 2013-1-28
 */
@Controller
@RequestMapping("/primary/activation.do")
public class ActivationPrimaryController extends ActivationBaseController {
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private UserService userService;
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private LoginService loginService;

	/**
	 * 激活登陆邮箱和密保邮箱，同时进行
	 * */
	@RequestMapping(params = "act=activeLoginEmailAndSecurity")
	public String activeLoginEmailAndSecurity(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ActivationForm data,
			BindingResult result) {
		String failUrl = getRedirectMessageUrl("active.login.email.fail");
		if (result.hasErrors()) {
			return failUrl;
		} else {
			boolean isValid = tokenService.isValid(data.getTokenid(),
					data.getRandom(),
					Token.OPERATION_ACTIVATION_PRIMARY_AND_SECURITY);
			if (isValid) {
				Token token = tokenService.getTokenById(data.getTokenid());
				SessionUtils.setSessionVar(request,
						Attributes.IS_USER_LOGIN_ACTIVE, true);
				LoginNameInfo nameInfo = loginNameService
						.getLoginNameInfoById(data.getLoginNameInfoId());
				if (nameInfo == null) {
					return failUrl;
				}
				User user = userService.getUserByUid(token.getUid());
				AbstractDoActivation doAction = new DoActivationServiceForPrimaryAndSecurity(
						request, response, token, user, data, nameInfo);
				return doAction.doActivation();
			}
			return failUrl;
		}
	}
	/**
	 * 激活登陆邮箱
	 **/
	@RequestMapping(params = "act=activeLoginEmail")
	public String activeLoginEmail(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ActivationForm data,
			BindingResult result) {
		boolean isValid = tokenService.isValid(data.getTokenid(),
				data.getRandom(), Token.OPERATION_ACTIVATION_PRIMARY_EMAIL);
		if (isValid) {
			Token token = tokenService.getTokenById(data.getTokenid());
			SessionUtils.setSessionVar(request,
					Attributes.IS_USER_LOGIN_ACTIVE, true);
			LoginNameInfo nameInfo = loginNameService.getLoginNameInfoById(data
					.getLoginNameInfoId());
			if (nameInfo == null) {
				return getRedirectMessageUrl("active.login.email.fail");
			}
			User user = userService.getUserByUid(token.getUid());
			AbstractDoActivation doActivation = new DoActivationServiceForPrimary(
					request, response, token, user, data, nameInfo);
			return doActivation.doActivation();

		}
		return getRedirectMessageUrl(isValid
				? "active.login.email.success"
				: "active.login.email.fail");
	}
	/**
	 * 确认更改主账户操作，以便接着更改
	 * */
	@RequestMapping(params = "act=confirmChangeLoginEmail")
	public String confirmChangeLoginEmail(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ActivationForm data,
			BindingResult result) {
		String failUrl = getRedirectMessageUrl("active.login.email.fail");
		if (result.hasErrors()) {
			return failUrl;
		}
		boolean isValid = tokenService.isValid(data.getTokenid(),
				data.getRandom(), Token.OPERATION_COMFIRM_PRIMARY_EMAIL);
		// token无效
		if (!isValid) {
			return failUrl;
		}
		Token token = tokenService.getTokenById(data.getTokenid());
		User user = userService.getUserByUid(token.getUid());
		tokenService.toUsed(token.getId());
		AbstractDoActivation doActivation = new DoActivationServiceForPrimaryConfirm(
				request, response, token, user, data);
		return doActivation.doActivation();

	}
	/**
	 * 更改主账户第一步，输入密码，登录
	 * */
	@RequestMapping(params = "act=doLoginPrimary")
	public String doLoginPrimary(HttpServletRequest request,
			HttpServletResponse response) {
		Token token = tokenService.getATokenByUidAndOperationWithExpire(
				SessionUtils.getUserId(request),
				Token.OPERATION_COMFIRM_PRIMARY_EMAIL, Token.STATUS_USED);
		if (token != null) {
			request.setAttribute("_primaryEmail",
					request.getParameter("primaryEmail"));
			return "/activation/login_name_step1_signin";
		} else {
			return getRedirectMessageUrl("active.login.email.fail");
		}
	}
	@RequestMapping(params = "act=doLoginPrimaryOnlyActive")
	public String doLoginPrimaryOnlyActive(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ActivationForm data,
			BindingResult result) {
		request.setAttribute("_primaryEmail",
				request.getParameter("primaryEmail"));
		request.setAttribute("_requestAct", request.getParameter("requestAct"));
		request.setAttribute("token", data);
		return "/activation/login_name_primary_active_signin";
	}
	/**
	 * 验证密码是否正确，准备跳入设置主邮箱页面
	 * */
	@RequestMapping(params = "act=validPasswordPrimary")
	public String validPasswordPrimary(HttpServletRequest request,
			HttpServletResponse response, @ModelAttribute ActivationForm data,
			BindingResult result) {
		String primaryEmail = request.getParameter("loginName");
		String password = request.getParameter("password");
		String requestAct = request.getParameter("requestAct");
		LoginInfo loginInfo = loginService
				.loginAndReturnPasswordType(new UsernamePasswordCredential(
						primaryEmail, password));
		if (loginInfo.getUser() != null) {
			UMTContext.saveUser(request.getSession(), loginInfo);
			if ("activeLogin".equals(requestAct)) {
				return activeLoginEmail(request, response, data, result);
			} else if ("activeLoginAndSecurity".equals(requestAct)) {
				return activeLoginEmailAndSecurity(request, response, data,
						result);
			} else {
				return "/user/primary/login_name_step2";
			}
		} else {
			request.setAttribute("_primaryEmail", primaryEmail);
			request.setAttribute("password_error",
					"security.email.password.error");
			if (CommonUtils.isNull(requestAct)) {
				return "/activation/login_name_step1_signin";
			} else {
				request.setAttribute("_requestAct", requestAct);
				request.setAttribute("_primaryEmail", primaryEmail);
				request.setAttribute("token", data);
				return "/activation/login_name_primary_active_signin";
			}
		}
	}
	// 最终提交，生效
	@RequestMapping(params = "act=finalComfirm")
	public String finalComfirm(HttpServletRequest request,
			HttpServletResponse response) {
		String loginName = request.getParameter("loginName");
		String primaryEmail = request.getParameter("primaryEmail");
		String password = request.getParameter("password");
		LoginInfo loginInfo = loginService
				.loginAndReturnPasswordType(new UsernamePasswordCredential(
						loginName, password));
		if (loginInfo.getUser() != null) {
			UMTContext.saveUser(request.getSession(), loginInfo);
			if (userService.isUsed(primaryEmail) != UserService.USER_NAME_UNUSED) {
				return getRedirectMessageUrl("email is qianged");
			}
			int uid = SessionUtils.getUserId(request);
			loginNameService.updateLoginName(uid, loginName, primaryEmail);
			loginNameService.updateToLoginName(uid, primaryEmail, null);
			loginNameService.toActive(uid, loginName,
					LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
			accountService.log(UMTLog.EVENT_TYPE_CHANGE_LOGIN_NAME, uid,
					RequestUtil.getRemoteIP(request),
					RequestUtil.getBrowseType(request));
			userService.updateValueByColumn(SessionUtils.getUserId(request),
					"cstnet_id", primaryEmail);
			request.setAttribute("email", primaryEmail);
			return "/activation/login_name_step4";
		} else {
			request.setAttribute("_oldEmail", loginName);
			request.setAttribute("_newPrimary", primaryEmail);
			request.setAttribute("password_error",
					"security.email.password.error");
			return "/activation/login_name_step3_check";
		}
	}

}