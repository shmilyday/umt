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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.ui.ShowPageController;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.ChangePasswordFormValidator;

@Controller
@RequestMapping("/user/password.do")
public class PasswordController {
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IAppSecretService appSecretService;
	@Autowired
	private UserService us;
	@Autowired
	private LoginService lservice;
	@Autowired
	private ILdapService ldapService;
	
	@RequestMapping(params = "act=isPasswordUsed")
	public void isPasswordUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String password = request.getParameter("password");
		int userId = SessionUtils.getUserId(request);
		boolean appSecretUsed = appSecretService.isAppSecretUsed(password,userId);
		response.getWriter().print(!appSecretUsed);
	}
	
	@RequestMapping(params = "act=showChangePassword")
	public String showChangePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String returnUrl = request.getHeader("Referer");
		if (request.getParameter("returnUrl") != null) {
			returnUrl = request.getParameter("returnUrl");
		}
		if (returnUrl != null && returnUrl.trim().length() > 0) {
			SessionUtils.setSessionVar(request, "returnUrl", returnUrl);
		}
		String passwordType = new UMTContext(request).getLoginInfo()
				.getPasswordType();
		if (LoginInfo.TYPE_THIRD_PARTY_QQ.equals(passwordType)
				|| LoginInfo.TYPE_THIRD_PARTY_SINA.equals(passwordType)) {
			response.sendRedirect(ShowPageController.getMessageUrl(request,
					"change.password.weibo.error"));
		}
		request.setAttribute("email", SessionUtils.getUser(request)
				.getCstnetId());
		return "/user/accountManage_changepassword";
	}
	@RequestMapping(params = "act=saveChangePassword")
	public String saveChangePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ErrorMsgs msgs = new ChangePasswordFormValidator(request)
				.validateForm();
		if (!msgs.isPass()) {
			return toErrorPage(request);
		}
		String oldpassword = request.getParameter("oldpassword");
		oldpassword = CommonUtils.killNull(oldpassword);
		String password = request.getParameter("password");
		User user = us.getUserByUid(SessionUtils.getUserId(request));
		boolean passwordRight = false;
		UsernamePasswordCredential credential = new UsernamePasswordCredential(user.getCstnetId(), oldpassword);
		if (user.isCoreMailOrUc()) {
			passwordRight = lservice.coreMailPasswordRight(credential);
			if (passwordRight) {
				us.updateCoreMailPassword(user.getCstnetId(), password);
			}
		} else {
			passwordRight = lservice.umtPasswrdRight(credential);
			if (passwordRight) {
				us.updatePassword(SessionUtils.getUserId(request), password);
			}
		}
		
		if (!passwordRight) {
			request.setAttribute("oldpasswordError", "true");
			return toErrorPage(request);
		}else{
			LdapBean wifiApp = ldapService.findAvailableWifi(user.getCstnetId());
			if (wifiApp!=null){
				//这里只能更新不能插入
				appSecretService.updateWifiPassword(wifiApp, user, password);
			}
		}
		String returnurl = (String) request.getSession().getAttribute(
				"returnUrl");
		if (returnurl == null) {
			returnurl = RequestUtil.getContextPath(request) + "/index.jsp";
			RequestUtil.addParam(returnurl, "msg", "change.password.success");
		} else {
			request.getSession().removeAttribute("returnUrl");
		}
		UMTContext context = new UMTContext(request);
		LoginInfo loginInfo = context.getLoginInfo();
		loginInfo.setWeak(false);
		UMTContext.saveUser(request.getSession(), loginInfo);
		accountService.log(UMTLog.EVENT_TYPE_CHANGE_PASSWORD,
				SessionUtils.getUserId(request),
				RequestUtil.getRemoteIP(request),
				RequestUtil.getBrowseType(request));
		response.sendRedirect(returnurl);
		return null;
	}
	private String toErrorPage(HttpServletRequest request) {
		request.setAttribute("email", new UMTContext(request).getLoginInfo()
				.getUser().getCstnetId());
		return "/user/accountManage_changepassword";
	}
}