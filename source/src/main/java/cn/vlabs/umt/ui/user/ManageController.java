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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.EmailUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.AppAccessPassword;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 账户管理
 * 
 * @author lvly
 * @since 2013-1-29
 */
@Controller
@RequestMapping("/user/manage.do")
public class ManageController {
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private IAppSecretService appSecretService;
	@Autowired
	private ILdapService ldapService;
	@Autowired
	private IOauthClientService oauthService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	/**
	 * 显示账户管理页面
	 * */
	@RequestMapping
	public String showManage(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/accountManage_manage";
	}
	@RequestMapping(params = "act=deletePwd")
	public void deletePwd(HttpServletRequest request,
			HttpServletResponse response) {
		String pwdIdStr = request.getParameter("pwdId");
		String appId = request.getParameter("appId");
		String viewType = request.getParameter("viewType");
		int uid = SessionUtils.getUserId(request);
		if ("ldap".equals(viewType)) {
			LoginNameInfo ldapLogin = loginNameService.getLdapLoginName(uid);
			ldapService.removeSoAccount(Integer.parseInt(appId),
					ldapLogin.getLoginName());
		}else if("wifi".equals(viewType)){
			UMTContext context = new UMTContext(request);
			String cstnetId = context.getCurrentUMTUser().getCstnetId();
			ldapService.removeSoAccount(Integer.parseInt(appId),EmailUtil.extractName(cstnetId));
		}
		appSecretService.deleteMySecret(Integer.parseInt(pwdIdStr), uid);
	}

	/**
	 * 显示应用猫猫
	 * */
	@RequestMapping(params = "act=appAccessPwd")
	public String appAccessPwd(HttpServletRequest request,
			HttpServletResponse response) {
		String viewType = request.getParameter("viewType");
		if (CommonUtils.isNull(viewType)) {
			viewType = "all";
		}
		request.setAttribute("viewType", viewType);
		List<AppSecret> appSecrets = appSecretService
				.findAppSecretByUid(SessionUtils.getUserId(request));
		Map<String, AppSecret> appSecretMap = CommonUtils.extractSthFieldToMap(
				appSecrets, "appId");

		switch(viewType){
			case "ldap":
				setLdapApps(request, viewType, appSecretMap);
				break;
			case "oauth":
				setOauthApps(request, appSecretMap);
				break;
			case "wifi":
				setLdapApps(request, viewType, appSecretMap);
				break;
			case "all":
				setLdapApps(request, "ldap", appSecretMap);
				setLdapApps(request, "wifi", appSecretMap);
				setOauthApps(request, appSecretMap);
				break;
		}
		return "/user/accountManage_appaccesspassword";
	}

	private void setLdapApps(HttpServletRequest request, String viewType,
			Map<String, AppSecret> appSecretMap) {
		int userId = SessionUtils.getUserId(request);
		LoginNameInfo loginInfo = CommonUtils.first(loginNameService
				.getLoginNameInfo(userId, LoginNameInfo.LOGINNAME_TYPE_LDAP));
		request.setAttribute("ldapName", loginInfo);
		User user = SessionUtils.getUser(request);
		List<LdapBean> ldapAll = ldapService.findEnableAndAccepted(viewType);
		List<LdapBean> beans = findUserEnableAndAccepted(ldapAll,
				user.getCstnetId(), appSecretMap.keySet());
		List<AppAccessPassword> aaps = new ArrayList<AppAccessPassword>();
		if (beans != null) {
			for (LdapBean ocb : beans) {
				aaps.add(new AppAccessPassword(ocb, appSecretMap.get(ocb
						.getId() + "")));
			}
		}
		LoginNameInfo nameInfo = loginNameService.getLdapLoginName(userId);
		request.setAttribute("nameInfo", nameInfo);
		if ("ldap".equals(viewType)){
			request.setAttribute("ldaps", aaps);
		}else{
			request.setAttribute("wifis", aaps);
		}
	}

	// 规则-如果用户已经开启应用独立密码则不管用户在不在应用的发布域都给用户显示
	private List<LdapBean> findUserEnableAndAccepted(
			List<LdapBean> queryResult, String userId,
			Collection<String> existsSecrteAppId) {
		if (StringUtils.isBlank(userId) || !StringUtils.contains(userId, "@")) {
			return new ArrayList<LdapBean>();
		}

		String userDaomain = StringUtils.substring(userId,
				StringUtils.indexOf(userId, "@") + 1, userId.length());

		if (StringUtils.isBlank(userDaomain)) {
			return new ArrayList<LdapBean>();
		}

		if (queryResult == null || queryResult.isEmpty()) {
			return queryResult;
		}

		List<LdapBean> result = new ArrayList<LdapBean>();
		for (LdapBean ladp : queryResult) {
			if (existsSecrteAppId.contains(ladp.getId() + "")) {
				result.add(ladp);
				continue;
			}

			String pubScope = ladp.getPubScope();
			if (StringUtils.isBlank(pubScope)) {
				result.add(ladp);
				continue;
			}

			String[] domains = StringUtils.split(pubScope, ";");
			for (String domain : domains) {
				if (StringUtils.equals(domain, userDaomain)) {
					result.add(ladp);
					break;
				}
			}
		}

		return result;
	}

	private void setOauthApps(HttpServletRequest request,
			Map<String, AppSecret> appSecretMap) {
		List<OauthClientBean> beans = oauthService
				.findEnableAppAndAccepted("all");
		List<AppAccessPassword> aaps = new ArrayList<AppAccessPassword>();
		if (beans != null) {
			for (OauthClientBean ocb : beans) {
				aaps.add(new AppAccessPassword(ocb, appSecretMap.get(ocb
						.getClientId())));
			}
		}
		request.setAttribute("oauths", aaps);
	}

	private void setDataWhenSetSecret(HttpServletRequest request, String appId,
			String viewType) {
		viewType = (CommonUtils.isNull(viewType)) ? request
				.getParameter("viewType") : viewType;
		appId = (CommonUtils.isNull(appId))
				? request.getParameter("appId")
				: appId;
		if (viewType == null || appId == null) {
			return;
		}
		User user = SessionUtils.getUser(request);
		int userId = SessionUtils.getUserId(request);
		AppSecret as = appSecretService.findAppSecretByUidAndAppId(appId,
				userId);
		AppAccessPassword app = null;
		switch (viewType) {
			case "wifi":{
				String loginName=user.getCstnetId();
				request.setAttribute("loginName", loginName);
			}
			case "ldap" : {
				LdapBean ldapBean = ldapService.getLdapBeanById(Integer.parseInt(appId));
				app = new AppAccessPassword(ldapBean, as);
				LoginNameInfo nameInfo = CommonUtils.first(loginNameService
						.getLoginNameInfo(userId,
								LoginNameInfo.LOGINNAME_TYPE_LDAP));
				request.setAttribute("nameInfo", nameInfo);
				break;
			}
			default : {
				OauthClientBean oauthClient = oauthService
						.findByClientId(appId);
				app = new AppAccessPassword(oauthClient, as);
				break;
			}
		}
		request.setAttribute("appId", appId);
		request.setAttribute("viewType", viewType);
		request.setAttribute("app", app);
	}

	/**
	 * 创建密码
	 * */
	@RequestMapping(params = "act=createPassword")
	public String createPassword(HttpServletRequest request,
			HttpServletResponse response) {
		setDataWhenSetSecret(request, null, null);
		request.setAttribute("op", request.getParameter("op"));
		return "/user/app_set_secret";
	}
	@RequestMapping(params = "act=savePassword")
	public String savePassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String appId = request.getParameter("appId");
		String viewType = request.getParameter("viewType");
		String password = request.getParameter("password");
		String ldapNameStr = CommonUtils.trim(request.getParameter("ldapName"));
		String secret = request.getParameter("appSecret");
		String retypeSecret=request.getParameter("appSecret_retype");
		if (CommonUtils.isNull(appId) || CommonUtils.isNull(viewType)
				|| CommonUtils.isNull(password) || CommonUtils.isNull(secret)) {
			return null;
		}
		UMTContext context = new UMTContext(request);
		String cstnetId = context.getCurrentUMTUser().getCstnetId();
		if (retypeSecret==null || !retypeSecret.equals(secret)){
			request.setAttribute("password_error",
					"app.pwd.validate.equals");
			request.setAttribute("ldapNameStr", ldapNameStr);
			setDataWhenSetSecret(request, appId, viewType);
			return "/user/app_set_secret";
		}
		if (!loginService.passwordRight(new UsernamePasswordCredential(
				cstnetId, password))) {
			request.setAttribute("password_error",
					"security.email.password.error");
			request.setAttribute("ldapNameStr", ldapNameStr);
			setDataWhenSetSecret(request, appId, viewType);
			return "/user/app_set_secret";
		}
		// 不允许与应用密码相同
		if (password.equals(secret)) {
			request.setAttribute("password_error",
					"app.pwd.validate.not.equals.to.password");
			request.setAttribute("ldapNameStr", ldapNameStr);
			setDataWhenSetSecret(request, appId, viewType);
			return "/user/app_set_secret";
		}
		
		String loginName;
		if ("wifi".equals(viewType)){
			loginName= EmailUtil.extractName(cstnetId);
		}else{
			LoginNameInfo loginInfo = loginNameService.getLdapLoginName(context
					.getCurrentUMTUser().getId());
			if (loginInfo == null && "ldap".equals(viewType)) {
				if (CommonUtils.isNull(ldapNameStr)) {
					return null;
				}
				if (loginNameService.isUsed(ldapNameStr)) {
					request.setAttribute("ldapName_error", "ldap.name.is.used");
					request.setAttribute("ldapNameStr", ldapNameStr);
					setDataWhenSetSecret(request, appId, viewType);
					return "/user/login_name_set_ldap_show";
				}
				loginNameService.createLoginName(ldapNameStr, context
						.getCurrentUMTUser().getId(),
						LoginNameInfo.LOGINNAME_TYPE_LDAP,
						LoginNameInfo.STATUS_ACTIVE);
				loginInfo = loginNameService.getLdapLoginName(SessionUtils
						.getUserId(request));
			}
			
			if(loginInfo!=null){
				
				loginName=loginInfo.getLoginName();
			}else{
				loginName="";
			}
			
			
		}
		
		LdapBean ldapBean = null;
		try {
			ldapBean = ldapService.getLdapBeanById(Integer.parseInt(appId));
		} catch (Exception e) {
		}
		AppSecret appSecret = new AppSecret();
		appSecret.setAppId(appId);
		appSecret.setSecretType(viewType);
		if (ldapBean != null) {
			appSecret.setUserStatus(LdapBean.PRIV_NEED_APPLY.equals(ldapBean
					.getPriv())
					? AppSecret.USER_STATUS_APPLY
					: AppSecret.USER_STATUS_ACCEPT);
		}

		appSecret.setRawPassword(secret);
		appSecret.setUid(SessionUtils.getUserId(request));
		appSecret.setUserName(context.getCurrentUMTUser().getTrueName());
		appSecret.setUserCstnetId(context.getCurrentUMTUser().getCstnetId());
		if(StringUtils.equals(viewType, "wifi")||StringUtils.equals(viewType, "ldap")){
			appSecret.setSecretType("ldap");
		}else{
			appSecret.setSecretType("oauth");
		}
		appSecret.setUserLdapName(loginName);
		appSecretService.updateOrInsertIfNotExist(appSecret, ldapBean);
		return "redirect:/user/manage.do?act=appAccessPwd&viewType="
				+ ((viewType.equals("ldap") ||viewType.equals("wifi"))? viewType : "oauth");
	}

	/**
	 * 显示更改密码页面
	 * */
	@RequestMapping(params = "act=showChangePassword")
	public String showChangePassword(HttpServletRequest request,
			HttpServletResponse response) {
		String returnUrl = request.getParameter("returnUrl");
		boolean showCoremailTip=StringUtils.equals(request.getParameter("showCoremailTip"), "true");
		if (!CommonUtils.isNull(returnUrl)) {
			SessionUtils.setSessionVar(request, "returnUrl", returnUrl);
		}
		request.setAttribute("showCoremailTip", showCoremailTip);
		return "/user/accountManage_changepassword";
	}

	/**
	 * 显示账户绑定页面
	 * */
	@RequestMapping(params = "act=showBindAccount")
	public String showBindAccount(HttpServletRequest request,
			HttpServletResponse response) {
		int userId = SessionUtils.getUserId(request);
		request.setAttribute("bindInfos", userService.getBindInfosByUid(userId));
		return "/user/accountManage_bindaccount";
	}

	/**
	 * 删除绑定
	 * */
	@RequestMapping(params = "act=deleteBind")
	public String deleteBind(HttpServletRequest request,
			HttpServletResponse response) {
		userService.deleteBindById(Integer.valueOf(request
				.getParameter("bindId")));
		return "redirect:/user/manage.do?act=showBindAccount";
	}

//	/**
//	 * 显示账户关联页面
//	 * */
//	@RequestMapping(params = "act=showAssociate")
//	public String showAssociate(HttpServletRequest request,
//			HttpServletResponse response) {
//		int userId = SessionUtils.getUserId(request);
//		boolean isAssociated = associateService.isAssociated(userId);
//		request.setAttribute("isAssociated", isAssociated);
//		AssociateUser user = associateService.getAssociate(userId);
//		request.setAttribute("associate", user);
//		request.setAttribute("associatedUser",userService.getUserByUid(user.getAssociateUid()));
//		return "/user/accountManage_associate";
//	}
}