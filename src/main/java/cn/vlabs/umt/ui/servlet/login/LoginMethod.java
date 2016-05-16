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
package cn.vlabs.umt.ui.servlet.login;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.commons.principal.UserPrincipalV7;
import cn.vlabs.duckling.common.transmission.SignedEnvelope;
import cn.vlabs.duckling.common.transmission.UserCredentialEnvelope;
import cn.vlabs.duckling.common.transmission.UserCredentialEnvelopeV7;
import cn.vlabs.duckling.common.util.Base64Util;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.auth.IAuthService;
import cn.vlabs.umt.services.auth.ThirdPartyAuth;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.role.UMTRole;
import cn.vlabs.umt.services.session.SessionService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.site.ApplicationNotFound;
import cn.vlabs.umt.services.user.Credential;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.CookieCredential;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.ThirdPartyCredential;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

public abstract class LoginMethod {
	private IAuthService authService;
	private IAccountService acct;
	public LoginMethod(BeanFactory factory) {
		this.factory = factory;
		acct = (IAccountService) factory.getBean("AccountService");
		authService = factory.getBean(IAuthService.class);
	}

	private void login(HttpServletRequest request, int uid, String authBy) {
		String loginType = null;
		if (CommonUtils.isNull(authBy)) {
			loginType = User.USER_TYPE_UMT;
		} else {
			loginType = authBy;
		}
		acct.login(loginType, null, uid, RequestUtil.getRemoteIP(request), new Date(), request.getHeader("User-Agent"),
				"");
	}

	public void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("act");
		String type = request.getParameter("type");
		if (BindInfo.isThirdParty(type) || authService.existAuth(type)) {
			insertThirdBind(request);
		}
		try {
			boolean result = loginByDeputy(request, response);
			if (!result && "Validate".equals(act)) {
				loginByInput(request, response);
			} else if (!result) {
				if (!loginBySession(request, response) && !loginByCookie(request, response)) {
					// 记录登录的应用
					request.setAttribute(Attributes.APP_NAME, request.getParameter(Attributes.APP_NAME));
					HttpSession session=request.getSession();
					String requireValid=(String) session.getAttribute("requireValid");
					if(StringUtils.isNotBlank(requireValid)&&StringUtils.equals(requireValid, "true")){
						request.setAttribute("showValidCode", "true");
					}
					redirectToLogin(request, response);
				}
			}
		} catch (ApplicationNotFound e) {
			request.setAttribute("error", e);
			doForward("/error.jsp", request, response);
		}
	}

	private boolean insertThirdBind(HttpServletRequest request) {
		String openId = SessionUtils.getSessionVar(request, Attributes.THIRDPARTY_OPEN_ID);
		if (!CommonUtils.isNull(openId)) {
			User user = ServiceFactory.getUserService(request).getUserByOpenid(openId,
					SessionUtils.getSessionVar(request, Attributes.THIRDPARTY_TYPE),
					SessionUtils.getSessionVar(request, Attributes.THIRDPARTY_URL));
			if (user != null) {
				request.setAttribute("username", user.getCstnetId());
				request.setAttribute("password", openId);
				return true;
			}
		}
		return false;
	}

	private String getThemedJSP(HttpServletRequest request, String jsp) {
		String theme = request.getParameter("theme");
		if (CommonUtils.isNull(theme)) {
			theme = (String) request.getSession().getAttribute("theme");
		}
		if (theme != null) {
			String calcedPath = "/themes/" + theme + jsp;
			File f = new File(request.getSession().getServletContext().getRealPath(calcedPath));
			if (f.exists() && f.isFile()) {
				return calcedPath;
			}
		}
		return jsp;
	}

	protected abstract void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	protected abstract boolean checkValidateCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	protected abstract void onWrongPassword(HttpServletRequest request, HttpServletResponse response,String wrongType)
			throws ServletException, IOException;

	private boolean loginByDeputy(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String loginByDeputy = request.getParameter("loginByDeputy");
		if (loginByDeputy != null) {
			Map<String, String> siteInfo = SessionUtils.getSiteInfo(request);
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String authBy = request.getParameter("authBy");
			if (StringUtils.isEmpty(password)) {
				password = "";
			}
			LoginInfo info = this.checkUser(username, password, authBy);
			User prins = (info != null) ? info.getUser() : null;
			if (prins == null) {
				siteInfo.put(Attributes.APP_DEPUTY_LOGIN_RESULT_KEY, "error");
			} else {
				siteInfo.put(Attributes.APP_DEPUTY_LOGIN_RESULT_KEY, "success");
				generateSsoCookie(response, request, info);
				this.saveThirdPartyCredential(request, authBy, username, password);
				generateAutoFill(response, request, info);
			}
			sendTicket(prins, siteInfo, request, response);
			return true;
		}
		return false;

	}

	private LoginInfo checkUser(String username, String password, String authBy) {
		LoginService lservice = (LoginService) factory.getBean(LoginService.BEAN_ID);
		String userName= username.toLowerCase();
		Credential credential = null;
		if (authBy == null || User.USER_TYPE_UMT.equals(authBy) || User.USER_TYPE_CORE_MAIL.equals(authBy)
				|| User.USER_TYPE_MAIL_AND_UMT.equals(authBy)) {
			credential = new UsernamePasswordCredential(userName, password);
		}
		// 比如新浪，扣扣，smtp之类的
		else {
			credential = new ThirdPartyCredential(userName, password, authBy);
		}
		LoginInfo prins = lservice.loginAndReturnPasswordType(credential);
		return prins;
	}

	private void loginByInput(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, ApplicationNotFound {
		String appnamePara = request.getParameter(Attributes.APP_NAME);
		HttpSession session = request.getSession();
		// 貌似appnamePara不为空即为第三方登陆，mirror除外
		if (CommonUtils.isNull(appnamePara)) {
			appnamePara = (String) session.getAttribute(Attributes.APP_NAME);
		}
		request.setAttribute(Attributes.APP_NAME, appnamePara);
		String authBy = request.getParameter("authBy");
		if (CommonUtils.isNull(authBy)) {
			authBy = (String) session.getAttribute("authBy");
		}
		request.setAttribute("authBy", authBy);
		if (!checkValidateCode(request, response)) {
			return;
		}
		// 检查用户名
		String username = request.getParameter("username");
		if (CommonUtils.isNull(username)) {
			username = (String) request.getAttribute("username");
		}
		String password = request.getParameter("password");
		if (CommonUtils.isNull(password)) {
			password = (String) request.getAttribute("password");
		}

		if (StringUtils.isEmpty(username)) {
			request.setAttribute("username", username);
			onWrongPassword(request, response,"true");
			return;
		}
		LoginInfo info = this.checkUser(username, password, authBy);
		User prins = (info != null) ? info.getUser() : null;
		if (prins == null) {
			request.setAttribute("username", username);
			onWrongPassword(request, response,info.getValidateResult());
			return;
		}
		// 保存第三用户凭证信息
		saveThirdPartyCredential(request, authBy, username, password);
		// 保存登录信息
		UMTContext.saveUser(session, info);
		// 拦截，如果用户为未激活用户，不能让他过去
		Map<String, String> siteInfo = SessionUtils.getSiteInfo(request);
		User user = SessionUtils.getUser(request);
		login(request, user.getId(), authBy);
		LoginNameInfo nameInfo = info.getLoginNameInfo();
		ThirdPartyAuth otherUmt=authService.find(authBy);
		info.setRequireUpgrade(otherUmt!=null || BindInfo.TYPE_QQ.equals(authBy));
		boolean isThirdBind = BindInfo.isThirdParty(authBy) || otherUmt!=null;
		if (!isThirdBind || CommonUtils.isNull(siteInfo)) {
			if (LoginNameInfo.STATUS_TEMP.equals(nameInfo.getStatus())) {
				SessionUtils.setSessionVar(request, "otherUmt", otherUmt);
				String showUrl = RequestUtil.getContextPath(request) + "/show.do";
				showUrl = RequestUtil.addParam(showUrl, "act", "showFilterActive");
				if (isThirdBind) {
					showUrl = RequestUtil.addParam(showUrl, "type", authBy);
				} else {
					showUrl = RequestUtil.addParam(showUrl, "type", user.getType());
				}

				showUrl = RequestUtil.addParam(showUrl, "oper", "login");
				showUrl = RequestUtil.addParam(showUrl, "sendEmail", user.getCstnetId());
				response.sendRedirect(showUrl);
				return;
			}
		}
		RoleService rs = (RoleService) factory.getBean(RoleService.BEAN_ID);
		UMTRole[] roles = rs.getUserRoles(username);
		UMTContext.saveRoles(session, roles);

		// 记住密码
		String remember = request.getParameter("remember");
		if ((remember != null) && (remember.equals("on") || remember.equals("yes"))) {
			generateSsoCookie(response, request, info);
		}
		generateAutoFill(response, request, info);
		// 加密用户凭证
		if (siteInfo != null && !siteInfo.isEmpty()&&!isThirdBind) {
			SessionUtils.setSessionVar(request, Attributes.SITE_INFO, null);
			sendTicket(prins, siteInfo, request, response);
		} else {
			String returnUrl=request.getParameter("returnUrl");
			if(siteInfo!=null&&StringUtils.isBlank(returnUrl)){
				returnUrl=siteInfo.get(Attributes.RETURN_URL);
			}
			//如果是弱密码，且使用coreMail密码登录，请跳转到修改密码
			if(info.isWeak()&&LoginInfo.TYPE_CORE_MAIL.equals(info.getPasswordType())){
				String redirectUrl=RequestUtil.getContextPath(request)+"/user/manage.do?act=showChangePassword&weakPassword=true";
				
				if(!CommonUtils.isNull(returnUrl)){
					redirectUrl+="&returnUrl="+URLEncoder.encode(returnUrl, "UTF-8"); 
				}
				response.sendRedirect(redirectUrl);
			}else{
				if(!CommonUtils.isNull(returnUrl)){
					response.sendRedirect(returnUrl);
				}else{
					response.sendRedirect(RequestUtil.getContextPath(request) + "/index.jsp");
				}
			}
		}
	}

	private void saveThirdPartyCredential(HttpServletRequest request, String authBy, String username, String password) {
		if (authBy != null &&!containEquals(new String[]{User.USER_TYPE_UMT,User.USER_TYPE_CORE_MAIL,User.USER_TYPE_MAIL_AND_UMT,BindInfo.TYPE_QQ,BindInfo.TYPE_CASHQ_SSO,BindInfo.TYPE_SINA,BindInfo.TYPE_UAF},authBy)) {
			ThirdPartyCredential tpc = new ThirdPartyCredential(username, password, authBy);
			request.getSession().setAttribute(Attributes.THIRDPARTY_CREDENTIAL, tpc);
		}
	}
	private boolean containEquals(String[] values,String value){
		for(String str:values){
			if(value.equals(str)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 产生自动保持登陆的cookie
	 * */
	public static void generateSsoCookie(HttpServletResponse response, HttpServletRequest request, LoginInfo loginInfo)
			throws UnsupportedEncodingException {
		PCookie pcookie = (PCookie) ServiceFactory.getBean(request, "PCookie");
		// 产生Pcookie
		String encrypted = pcookie.encrypt(loginInfo.getUser().getCstnetId() + "/" + RequestUtil.getRemoteIP(request)
				+ "/" + loginInfo.getPasswordType() + "/" + System.currentTimeMillis());
		Cookie cookie = new Cookie(Attributes.COOKIE_NAME, encrypted);
		cookie.setPath("/");
		cookie.setMaxAge(MAX_COOKIE_AGE);
		response.addCookie(cookie);
		Cookie umtIdCookie = new Cookie(Attributes.SSO_FLAG, SessionUtils.getUserId(request) + "");
		umtIdCookie.setDomain(Attributes.SSO_FLAG_DOMAIN);
		umtIdCookie.setPath("/");
		umtIdCookie.setMaxAge(LoginMethod.MAX_COOKIE_AGE);
		response.addCookie(umtIdCookie);
	}

	public static void generateAutoFill(HttpServletResponse response, HttpServletRequest request, LoginInfo loginInfo) {
		Cookie autoFill = new Cookie(Attributes.AUTO_FILL, loginInfo.getLoginNameInfo().getLoginName());
		autoFill.setPath("/");
		autoFill.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(autoFill);
	}

	/**
	 * 返回cookie里的cookie信息
	 */
	public static String getSsoCookieValue(HttpServletRequest request) {
		PCookie pcookie = (PCookie) ServiceFactory.getBean(request, "PCookie");
		Cookie cookie = getCookieByName(request, Attributes.COOKIE_NAME);
		if (cookie == null) {
			return null;
		}
		return pcookie.decrypt(cookie.getValue());
	}

	public static Cookie getCookieByName(HttpServletRequest request, String cookieName) {
		Cookie fcookie = null;
		if (cookieName != null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookieName.equals(cookie.getName())) {
						fcookie = cookie;
						break;
					}
				}
			}
		}
		return fcookie;
	}

	private boolean loginByCookie(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, ApplicationNotFound {
		Cookie cookie = getCookieByName(request, Attributes.COOKIE_NAME);
		if (cookie == null) {
			return false;
		}
		if (StringUtils.isNotEmpty(cookie.getValue())) {
			String cookieValue = getSsoCookieValue(request);
			if (cookieValue != null) {
				LoginInfo info = ServiceFactory.getLoginService(request).loginAndReturnPasswordType(
						new CookieCredential(cookieValue, RequestUtil.getRemoteIP(request)));
				User userPrincipal = (info != null) ? info.getUser() : null;
				if (userPrincipal != null) {

					saveThirdPartyCredential(request, userPrincipal.getType(), cookieValue, "");
					HttpSession session = request.getSession();
					session.setAttribute(Attributes.LOGIN_INFO, info);
					// Roles
					RoleService rs = (RoleService) factory.getBean("RoleService");
					UMTRole[] roles = rs.getUserRoles(cookieValue);
					UMTContext.saveRoles(session, roles);
					generateSsoCookie(response, request, info);
					generateAutoFill(response, request, info);
					String appname = request.getParameter(Attributes.APP_NAME);
					if (appname == null)
					{
						appname = (String) request.getAttribute(Attributes.APP_NAME);
					}

					if (appname != null) {
						sendTicket(userPrincipal, SessionUtils.getSiteInfo(request), request, response);
					} else {
						response.sendRedirect(RequestUtil.getContextPath(request) + "/index.jsp");
					}
					return true;
				}
				request.setAttribute("cookieError", "true");
			}
		}
		return false;
	}

	private boolean loginBySession(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException, ApplicationNotFound {
		HttpSession session = request.getSession();
		LoginInfo loginInfo = UMTContext.getLoginInfo(session);
		User up = loginInfo.getUser();
		if (up != null) {
			String appname = request.getParameter(Attributes.APP_NAME);
			if (appname == null)
			{
				appname = (String) request.getAttribute(Attributes.APP_NAME);
			}
			if (appname != null) {
				sendTicket(up, SessionUtils.getSiteInfo(request), request, response);
			} else {
				response.sendRedirect(RequestUtil.getContextPath(request) + "/index.jsp");
			}
			return true;
		}
		return false;
	}

	private UserPrincipalV7 user2UserPrincipalV7(User user) {
		if (user == null) {
			return null;
		}
		UserPrincipalV7 en = new UserPrincipalV7();
		en.setAuthBy(user.getType());
		en.setPrimaryEmail(user.getCstnetId());
		en.setSecondaryEmails(user.getSecondaryEmails());
		en.setSecurityEmail(user.getSecurityEmail());
		en.setTrueName(user.getTrueName());
		en.setUmtId(user.getUmtId());
		return en;
	}

	private void sendTicket(User up, Map<String, String> siteInfo, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean isVersion7 = "7".equals(request.getParameter(Attributes.UMT_VERSION));
		UMTCredential cred = (UMTCredential) factory.getBean("UMTCredUtil");
		SignedEnvelope env = null;
		if (isVersion7) {
			UserCredentialEnvelopeV7 userCredential = new UserCredentialEnvelopeV7();
			userCredential.setAuthAppId(cred.getUMTId());
			userCredential.setUser(user2UserPrincipalV7(up));
			Date tommrow = DateUtils.addDays(new Date(), 1);
			userCredential.setValidTime(DateFormatUtils.format(tommrow, "yyyy-MM-dd hh:mm:ss"));
			env = new SignedEnvelope(userCredential.toXML(), cred.getUMTId());
		} else {
			UserCredentialEnvelope userCredential = new UserCredentialEnvelope();
			userCredential.setAuthAppId(cred.getUMTId());
			userCredential.setUser(up == null ? null : up.getUserPrincipal());
			Date tommrow = DateUtils.addDays(new Date(), 1);
			userCredential.setValidTime(DateFormatUtils.format(tommrow, "yyyy-MM-dd hh:mm:ss"));
			env = new SignedEnvelope(userCredential.toXML(), cred.getUMTId());
		}
		env.genSignature(cred.getUMTKey());
		request.setAttribute("signedCredential", Base64Util.encodeBase64(env.toXML()));

		SessionService seService = (SessionService) factory.getBean("SessionService");
		String loginByDeputyResult = siteInfo.get(Attributes.APP_DEPUTY_LOGIN_RESULT_KEY);
		if (loginByDeputyResult != null) {
			request.setAttribute(Attributes.APP_DEPUTY_LOGIN_RESULT_KEY, loginByDeputyResult);
		}
		if (loginByDeputyResult == null || "success".equals(loginByDeputyResult)) {
			seService.login(siteInfo.get(Attributes.APP_NAME), up.getCstnetId(), RequestUtil.getRemoteIP(request),
					siteInfo.get(Attributes.LOGOUT_URL), "JSP", siteInfo.get(Attributes.SESSION_ID_KEY));
		}
		request.setAttribute(Attributes.RETURN_URL, siteInfo.get(Attributes.RETURN_URL));
		ServiceFactory.getAppAccessService(request).createAppAccess(up == null ? -1 : up.getId(),
				siteInfo.get(Attributes.APP_NAME));
		doForward("/post.jsp", request, response);
	}

	protected void doForward(String url, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String mappedURL = getThemedJSP(request, url);
		RequestDispatcher rd = request.getSession().getServletContext().getRequestDispatcher(mappedURL);
		rd.forward(request, response);
	}

	protected BeanFactory factory;

	// 24 hours*14
	public  static final int MAX_COOKIE_AGE = 10 * 60 * 60 * 24;
}