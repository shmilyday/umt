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

import net.duckling.cloudy.common.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 更改登录名操作,需要登陆
 * 
 * @author lvly
 * @since 2013-3-4
 */
@Controller
@RequestMapping("/user/ldap/loginName.do")
public class LdapLoginNameController {
	@Autowired
	private IUserLoginNameService userLoginNameService;
	@Autowired
	private IAppSecretService appSecretService;
	@Autowired
	private LoginService loginService;
	/**
	 * 显示设置终端账号界面
	 * */
	@RequestMapping(params = "act=setLdapName")
	public String setLdapName(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/ldap/login_name_set_ldap_show";
	}

	/**
	 * 验证ldap用户是否被使用
	 * 
	 * @throws IOException
	 * */
	@RequestMapping(params = "act=validateLdapNameUsed")
	public void validateLdapNameUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String newName = CommonUtils.trim(request.getParameter("ldapName"));
		boolean flag = userLoginNameService.isUsed(newName);
		response.getWriter().print(!flag);
	}
	/**
	 * 删除
	 * */
	@RequestMapping(params = "act=deleteLdapName")
	public String deleteLdapName(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		UMTContext context = new UMTContext(request);
		LoginNameInfo lni = userLoginNameService.getLdapLoginName(context
				.getCurrentUMTUser().getId());
		userLoginNameService.removeLdapLoginName(context.getCurrentUMTUser()
				.getId());
		appSecretService.removeAllLdapSecret(context.getCurrentUMTUser()
				.getId(), lni.getLoginName());
		return "redirect:/user/manage.do?act=showManage";
	}
	/**
	 * 更新ldap用户名
	 * */
	@RequestMapping(params = "act=updateLdapName")
	public String updateLdapName(HttpServletRequest request,
			HttpServletResponse response) {
		UMTContext context = new UMTContext(request);
		request.setAttribute("ldapName", CommonUtils.first(userLoginNameService
				.getLoginNameInfo(context.getCurrentUMTUser().getId(),
						LoginNameInfo.LOGINNAME_TYPE_LDAP)));
		return "/user/ldap/login_name_set_ldap_update";
	}
	@RequestMapping(params = "act=submitLdapNameUpdate")
	public String submitLdapNameUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		UMTContext context = new UMTContext(request);
		String cstnetId = context.getCurrentUMTUser().getCstnetId();
		String password = request.getParameter("password");
		String ldapName = CommonUtils.trim(request.getParameter("ldapName"));
		LoginNameInfo loginInfo = CommonUtils.first(userLoginNameService
				.getLoginNameInfo(context.getCurrentUMTUser().getId(),
						LoginNameInfo.LOGINNAME_TYPE_LDAP));
		if (!loginService.passwordRight(new UsernamePasswordCredential(
				cstnetId, password))) {
			request.setAttribute("password_error",
					"security.email.password.error");
			request.setAttribute("ldapNameStr", ldapName);
			return "/user/ldap/login_name_set_ldap_update";
		}

		if (userLoginNameService.isUsed(ldapName)) {
			request.setAttribute("ldapName_error", "ldap.name.is.used");
			request.setAttribute("ldapNameStr", ldapName);
			return "/user/ldap/login_name_set_ldap_update";
		};
		request.setAttribute("ldapName", loginInfo);
		userLoginNameService.updateLoginName(context.getCurrentUMTUser()
				.getId(), loginInfo.getLoginName(), ldapName);
		appSecretService.removeAllLdapSecret(SessionUtils.getUserId(request),
				loginInfo.getLoginName());
		return "redirect:/user/manage.do?act=showManage";
	}
	/**
	 * 保存新用户名
	 * */
	@RequestMapping(params = "act=saveLdapName")
	public String saveLdapName(HttpServletRequest request,
			HttpServletResponse response) {
		UMTContext context = new UMTContext(request);
		String cstnetId = context.getCurrentUMTUser().getCstnetId();
		String password = request.getParameter("password");

		String ldapName = CommonUtils.trim(request.getParameter("ldapName"));
		if (!loginService.passwordRight(new UsernamePasswordCredential(
				cstnetId, password))) {
			request.setAttribute("password_error",
					"security.email.password.error");
			request.setAttribute("ldapNameStr", ldapName);
			return "/user/ldap/login_name_set_ldap_show";
		}

		if (userLoginNameService.isUsed(password)) {
			request.setAttribute("ldapName_error", "ldap.name.is.used");
			request.setAttribute("ldapNameStr", ldapName);
			return "/user/ldap/login_name_set_ldap_show";
		};

		userLoginNameService.createLoginName(ldapName, context
				.getCurrentUMTUser().getId(),
				LoginNameInfo.LOGINNAME_TYPE_LDAP, LoginNameInfo.STATUS_ACTIVE);
		return "redirect:/user/manage.do?act=showManage";
	}
}