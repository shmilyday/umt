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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.oauth.as.issuer.MD5Generator;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuer;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuerImpl;
import cn.vlabs.umt.oauth.as.request.OAuthAuthzRequest;
import cn.vlabs.umt.oauth.as.response.OAuthASResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
import cn.vlabs.umt.oauth.common.message.OAuthResponse;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.AuthorizationCodeBean;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OAuthAuthzRequestWrap;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.OauthScopeBean;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAuthorizationCodeServer;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.IOauthTokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.servlet.login.LoginMethod;

/**
 * oauth进入servlet
 * @author zhonghui
 *
 */
public class AuthorizationCodeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1523764840219050925L;
	private static final Logger LOG = Logger.getLogger(AuthorizationCodeServlet.class);
	private static final String USER_OAUTH_REQUEST="user_oauth_request";
	private static long authorTimeout = 5; 
	private IOauthClientService oauthClientServer;
	private IOauthTokenService oauthTokenServer;
	private IAuthorizationCodeServer authorizationCodeServer;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		addCrossDomainHeader(response);
		String pageInfo = request.getParameter("pageinfo");
		if(StringUtils.isEmpty(pageInfo)){
			authorization(request, response);
		}else if("userinfo".equals(pageInfo)){
			validationUser(request, response,null);
		}else if("userscope".equals(pageInfo)){
			sendAuthorization(request,response,null);
		}else if("cancelauthorization".equals(pageInfo)){
			cancelAuthorization(request,response);
		}else if("checkPassword".equals(pageInfo)){
			checkPassword(request,response);
		}else if("checkLogin".equals(pageInfo)){
			checkLogin(request,response);
		}else{
			response.setStatus(404);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	
	/**
	 * 第一步
	 * 授权请求开始，校验用户Authorization数据格式，处理异常，并控制数据页面
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void authorization(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		String redirectURI = null;
		try {
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			String clientId = oauthRequest.getClientId();
			Set<String> scope = oauthRequest.getScopes();
			redirectURI = oauthRequest.getRedirectURI();
			if(!validateClient(clientId,  redirectURI,request,response)){
				return;
			}
			OauthClientBean bean = getClientServer().findByClientId(clientId);
			request.setAttribute("client_name", bean.getClientName());
			request.setAttribute("client_website", bean.getClientWebsite());
			if(!validateScope(clientId,scope)){
				dealAppError("invalid_scope","scope["+scope+"]校验错误", redirectURI, response);
				return;
			}
			request.setAttribute(USER_OAUTH_REQUEST, new OAuthAuthzRequestWrap(oauthRequest,request));
			if(userHaveLogin(request,response)){
				return;
			}
			dealCoremailUserName(request);
			request.setAttribute("thirdPartyList", bean.getThirdPartyMap());
			Map<String,String> siteInfo=new HashMap<String,String>();
			siteInfo.put(Attributes.RETURN_URL, URLEncoder.encode(RequestUtil.getFullRequestUrl(request),"UTF-8"));
			siteInfo.put(Attributes.APP_NAME,"umtOauth2");
			SessionUtils.setSessionVar(request, Attributes.SITE_INFO, siteInfo);
			forwordUserInfoPage(request, response);
		} catch (OAuthProblemException ex) {
			if(StringUtils.isEmpty(redirectURI)){
				redirectURI = request.getParameter("redirect_url");
				if(StringUtils.isEmpty(redirectURI)){
					request.setAttribute("client_id", request.getParameter("client_id"));
					request.setAttribute("errorCode", ex.getError());
					request.setAttribute("errorDescription", ex.getDescription());
					dealClientRedirectError(request, response);
					return;
				}
			}
			OAuthResponse resp = null;
			try {
				resp = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_FOUND).error(ex)
						.location(redirectURI).buildQueryMessage();
			} catch (OAuthSystemException e) {
				LOG.error("",e);
			}
			response.sendRedirect(resp.getLocationUri());
			LOG.info("redirect="+redirectURI,ex);
		} catch (OAuthSystemException e) {
			LOG.error("",e);
			dealOAuthSystemError(redirectURI, e,request,response);
		}
	}
	
	/**
	 * 处理记住用户名
	 * @param request
	 */
	private void dealCoremailUserName(HttpServletRequest request) {
		Cookie[] cs = request.getCookies();
		if(cs!=null){
			for(Cookie c : cs){
				if("passport.remember.user".equals(c.getName())){
					if(StringUtils.isNotEmpty(c.getValue())){
						request.setAttribute("userName", c.getValue());
					}
				}
			}
		}
	}


	/**
	 * 允许IE跨域访问
	 * @param response
	 */
	private void addCrossDomainHeader(HttpServletResponse response){
		response.setHeader(	"P3P","CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
	}
	
	private void forwordUserInfoPage(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		String theme = request.getParameter("theme");
		if("full".equals(theme)){
			request.getRequestDispatcher("/oauth/login_full.jsp").forward(request, response);
		}else if("simple".equals(theme)){
			request.getRequestDispatcher("/oauth/login_simple.jsp").forward(request, response);
		}else if("embed".equals(theme)){
			request.getRequestDispatcher("/oauth/login_embed.jsp").forward(request, response);
		}else if("coremail".equals(theme)){
			request.getRequestDispatcher("/oauth/login_coremail.jsp").forward(request, response);
		}else if("coremail_mobile".equals(theme)){
			request.getRequestDispatcher("/oauth/login_coremail_mobile.jsp").forward(request, response);
		}else if("coremail_mobile_ipad".equals(theme)){
			request.setAttribute("ipadFlag", true);
			request.getRequestDispatcher("/oauth/login_coremail_mobile.jsp").forward(request, response);
		}else if("cstnet_wifi".equals(theme)){
			request.getRequestDispatcher("/oauth/login_cstnet_wifi.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher("/oauth/login_full.jsp").forward(request, response);
		}
	}
	
	/**
	 * 如果用户已经登录直接跳转到权限设置方法
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private boolean userHaveLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginInfo loginInfo = getLoginInfoFromCookieAndSession(request);
		if(loginInfo.getUser()!=null){
			//如果用户已经登录了，会直接到下环节或者跳回页面进行页面自动登录过程
			String theme = request.getParameter("theme");
			if(!isEmbedPage(theme)){
				validationUser(request, response,(OAuthAuthzRequestWrap)request.getAttribute(USER_OAUTH_REQUEST));
				return true;
			}else{
				request.setAttribute("login", true);
			}
		}
		return false;
	}
	
	/**
	 * 是否页面使用的是嵌入框
	 * @param theme
	 * @return
	 */
	private boolean isEmbedPage(String theme){
		if(StringUtils.isEmpty(theme)){
			return false;
		}
		return "embed".equals(theme)||theme.startsWith("coremail")||"cstnet_wifi".equals(theme);
		
	}

	/**
	 * 通过session和cookie判断用户是否登录，如果登录就刷新session
	 * @param request
	 * @return
	 */
	private LoginInfo getLoginInfoFromCookieAndSession(HttpServletRequest request) {
		LoginInfo loginInfo = UMTContext.getLoginInfo(request.getSession());
		return loginInfo;
	}
	private boolean validateScope(String clientId, Set<String> scope) {
		IOauthClientService server = getClientServer();
		OauthClientBean bean = server.findByClientId(clientId);
		if(bean!=null){
			return bean.validateScope(scope);
		}
		return false;
	}

	
	/**
	 * 处理并返回异常结果
	 * @param errorCode 异常类型
	 * @param redirectURI
	 * @param response
	 * @throws IOException
	 */
	private void dealAppError(String errorCode,String desc,String redirectURI,HttpServletResponse response) throws IOException{
		OAuthResponse resp = null;
		try {
			resp = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND)
					.setError(errorCode)
					.setErrorDescription(desc)
					.location(redirectURI)
					.buildQueryMessage();
		} catch (OAuthSystemException e) {
			LOG.error("", e);
		}
		LOG.info("redirectURI="+redirectURI+"发生了errorCode="+errorCode+";description="+desc+";的错误");
		response.sendRedirect(resp.getLocationUri());
	}
	
	/**
	 * 校验客户端传来的参数
	 * @param clientId
	 * @param secret
	 * @param redirectURI
	 * @return
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private boolean validateClient(String clientId,String redirectURI,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		IOauthClientService server = getClientServer();
		OauthClientBean bean = server.findAcceptByClientId(clientId);
		try{
			new URI(redirectURI);
		}catch(Exception e){
			request.setAttribute("redirect_uri", redirectURI);
			request.setAttribute("client_id", clientId);
			request.setAttribute("errorCode", "invalid_request");
			request.setAttribute("errorDescription", "redirect_uri格式不正确");
			dealClientRedirectError(request, response);
			return false;
		}
		if(bean==null||!bean.getClientId().equals(clientId)){
			dealAppError("unauthorized_client","client_id["+clientId+"]未获取授权" ,redirectURI, response);
			return false;
		}
		if(!bean.getRedirectURI().equals(redirectURI)){
			dealAppError("redirect_uri_mismatch","redirectURI["+bean.getRedirectURI()+"]与服务器配置的不一致", redirectURI, response);
			return false;
		}
		return true;
	}
	
	/**
	 * 第二步<br/>
	 * 授权请求处理，认证用户信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void validationUser(HttpServletRequest request,HttpServletResponse response,OAuthAuthzRequestWrap oauthRequest) throws ServletException, IOException{
		if(oauthRequest==null){
			oauthRequest = new OAuthAuthzRequestWrap(request);
		}
		UserPrincipal info = getLoginInfo(request,getClientServer().findByClientId(oauthRequest.getClientId()));
		if(info==null){
			request.setAttribute("userName", request.getParameter("userName"));
			request.setAttribute("password", request.getParameter("password"));
			if(StringUtils.isEmpty(request.getParameter("userName"))){
				request.setAttribute("userNameNull", true);
			}else if(StringUtils.isEmpty(request.getParameter("password"))){
				request.setAttribute("passwordNull", true);
			}else{
				request.setAttribute("loginerror", true);
			}
			if(!validateClient(oauthRequest.getClientId(),  oauthRequest.getRedirectURI(),request,response)){
				return;
			}
			OauthClientBean bean = getClientServer().findByClientId(oauthRequest.getClientId());
			request.setAttribute("client_name", bean.getClientName());
			request.setAttribute("client_website", bean.getClientWebsite());
			request.setAttribute(USER_OAUTH_REQUEST, oauthRequest);
			forwordUserInfoPage(request, response);
		}else{
			request.setAttribute(USER_OAUTH_REQUEST, oauthRequest);
			request.setAttribute("client_id", oauthRequest.getClientId());
			doCoremailRequest(request,response);
			LoginInfo userLogin = UMTContext.getLoginInfo(request.getSession());
			if(StringUtils.isNotEmpty(request.getParameter("remember"))){
				LoginMethod.generateSsoCookie(response, request, userLogin);
			}
			LoginMethod.generateAutoFill(response,request,userLogin);
			Set<String> s =getClientServer().findByClientId(oauthRequest.getClientId()).getScopeSet();
			//如果client没有scope就直接返回
			if(s==null||s.isEmpty()){
				sendAuthorization(request, response, oauthRequest);
				return;
			}
			request.setAttribute("scopes", dealScope(oauthRequest.getClientId(), oauthRequest.getScopes()));
			request.getRequestDispatcher("/oauth/userscopeinfo.jsp").forward(request, response);
		}
	}
	
	private void doCoremailRequest(HttpServletRequest request,HttpServletResponse response) {
		String theme = request.getParameter("themeinfo");
		if(StringUtils.isNotEmpty(theme)&&theme.startsWith("coremail")){
			if(StringUtils.isNotEmpty(request.getParameter("rememberUserName"))){
				String userName = request.getParameter("userName");
				if(StringUtils.isNotEmpty(userName)){
					Cookie cookie = new Cookie("passport.remember.user",userName);
					cookie.setPath("/");
					cookie.setMaxAge(14*60 * 60 * 24);
					response.addCookie(cookie);
				}
			}else{
				//清除cookie
				Cookie cookie = new Cookie("passport.remember.user","");
				cookie.setPath("/");
				cookie.setMaxAge(1);
				response.addCookie(cookie);
			}
			if(StringUtils.isNotEmpty(request.getParameter("secureLogon"))){
				request.getSession().setAttribute("coremailSecureLogon", true);
			}
		}
	}


	private void checkPassword(HttpServletRequest request, HttpServletResponse response) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		boolean result = false;
		if(StringUtils.isNotEmpty(userName)&&StringUtils.isNotEmpty(password)){
			UsernamePasswordCredential cred = new UsernamePasswordCredential(userName,password);
			result = ServiceFactory.getLoginService(request).passwordRight(cred);
		}
		JSONObject obj = new JSONObject();
		obj.put("status", String.valueOf(result));
		writeJSONResponse(response, obj);
		
	}



	public void writeJSONResponse(HttpServletResponse response, JSONObject obj) {
		PrintWriter writer = null;
		try {
			//为了兼容IE系浏览器，特意设置成text/html格式
			response.setContentType("text/html");
			writer = response.getWriter();
			writer.write(obj.toString());
		} catch (IOException e) {
			LOG.error("JSONHelper write json object IOException:"+e.getMessage());
			LOG.debug(e.getStackTrace());
		}finally {
			if (writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}
	
	/**
	 * 第三步
	 * 处理用户的scope数据，并返回授权结果
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	private void sendAuthorization(HttpServletRequest request, HttpServletResponse response,OAuthAuthzRequestWrap oauthRequest) throws IOException, ServletException {
		if(oauthRequest==null){
			oauthRequest = new OAuthAuthzRequestWrap(request);
		}
		String responseType = oauthRequest.getResponseType();
		if("code".equals(responseType)){
			responseTypeIsCode(request, response, oauthRequest);
		}else if("token".equals(responseType)){
			responseTypeIsToken(request, response, oauthRequest);
		}else{
			dealAppError("unsupported_response_type","response_type["+ responseType+"]请求的响应类型授权服务器不支持", oauthRequest.getRedirectURI(), response);
		}
		cleanSession(request);
	}

	private void cleanSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("coremailSecureLogon");
	}


	/**
	 * 处理responseType为token类型
	 * @param request
	 * @param response
	 * @param oauthRequest
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void responseTypeIsToken(HttpServletRequest request, HttpServletResponse response,
			OAuthAuthzRequestWrap oauthRequest) throws IOException, ServletException {
		String[] scopes = request.getParameterValues("userScopes");
		String redirectURI =  oauthRequest.getRedirectURI();
		try {
			String uid = ServiceFactory.getUserService(request).getUserByLoginName(getLoginInfo(request,getClientServer().findByClientId(oauthRequest.getClientId())).getName()).getId()+"";
			OauthToken token = OauthTokenServlet.createToken(oauthRequest.getClientId(), redirectURI, request, tansferScope(scopes), uid);
			StringBuilder sb = new StringBuilder();
			addTokenParam(sb,token,request,oauthRequest.getState());
			if(redirectURI.contains("#")){
				if(redirectURI.endsWith("#")){
					redirectURI=redirectURI+sb.toString();
				}else{
					redirectURI=redirectURI+"&"+sb.toString();
				}
			}else{
				redirectURI=redirectURI+"#"+sb.toString();
			}
			getTokenServer().save(token);
			response.sendRedirect(redirectURI);
		} catch (OAuthSystemException e) {
			dealOAuthSystemError(redirectURI, e,request, response);
		}
	}

	private void addTokenParam(StringBuilder sb, OauthToken token,HttpServletRequest request,String state) {
		User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
		LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
		LoginInfo userLogin = UMTContext.getLoginInfo(request.getSession());
		String userInfo = OauthTokenServlet.getUserAsJSON(loginInfo, user,userLogin.getPasswordType());
		sb.append("access_token=").append(encodeURL(token.getAccessToken()));
		sb.append("&");
		sb.append("expires_in=").append(encodeURL(getExpired(token.getAccessExpired())+""));
		if(StringUtils.isNotEmpty(state)){
			sb.append("&").append("state=").append(encodeURL(state));
		}
		sb.append("&").append("userInfo=").append(encodeURL(userInfo));
	}
	private long getExpired(Date date){
		long re = date.getTime()-System.currentTimeMillis();
		return (re/1000);
	}
	
	private String encodeURL(String url){
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}


	/**
	 * 处理responseType为code类型
	 * @param request
	 * @param response
	 * @param oauthRequest
	 * @throws IOException
	 * @throws ServletException 
	 */
	private void responseTypeIsCode(HttpServletRequest request, HttpServletResponse response,
			OAuthAuthzRequestWrap oauthRequest) throws IOException, ServletException {
		String[] scopses = request.getParameterValues("userScopes");
		String redirectURI = getRedirectURI(request,oauthRequest);
		OAuthResponse resp;
		try {
			LoginInfo userLogin = UMTContext.getLoginInfo(request.getSession());
			AuthorizationCodeBean bean = createAuthCodeBean(userLogin, oauthRequest);
			bean.updateScope(scopses);
			//设置过期时间
			bean.setExpiredTime( new Date(System.currentTimeMillis()+authorTimeout*60l*1000l));
			getCodeServer().save(bean);
			resp = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND)
					.setCode(bean.getCode())
					.setParam("state", bean.getState())
					.location(redirectURI).buildQueryMessage();
			response.sendRedirect(resp.getLocationUri());
		} catch (OAuthSystemException e) {
			dealOAuthSystemError(redirectURI, e,request, response);
		}
	}
	
	private String getRedirectURI(HttpServletRequest request, OAuthAuthzRequestWrap oauthRequest) {
		Boolean b = (Boolean)request.getSession().getAttribute("coremailSecureLogon");
		String theme =oauthRequest.getTheme();
		String redirectURL = oauthRequest.getRedirectURI();
		if("coremail".equals(theme)){
			if(b!=null&&b){
				redirectURL = redirectURL.trim();
				if(redirectURL.startsWith("http:")){
					return "https"+redirectURL.substring(4);
				}
			}
		}
		return redirectURL;
	}


	private AuthorizationCodeBean createAuthCodeBean(LoginInfo userLogin, OAuthAuthzRequestWrap oauthRequest)
			throws OAuthSystemException {
		User user=userLogin.getUser();
		AuthorizationCodeBean bean = new AuthorizationCodeBean();
		String passwordType = userLogin.getPasswordType();
		bean.setClientId(oauthRequest.getClientId());
		bean.setPasswordType(passwordType);
		bean.setRedirectURI(oauthRequest.getRedirectURI());
		bean.setScope(tansferScope(oauthRequest.getScopes()));
		bean.setState(oauthRequest.getState());
		bean.setUid(user.getId());
		bean.setCreateTime(new Date());
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		bean.setCode(oauthIssuerImpl.authorizationCode());
		return bean;
	}
	
	private UserPrincipal getLoginInfo(HttpServletRequest request,OauthClientBean bean) {
		LoginInfo userLogin = UMTContext.getLoginInfo(request.getSession());
		User user=userLogin.getUser();
		if(user == null){
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
				return null;
			}
			UsernamePasswordCredential cred = new UsernamePasswordCredential(userName,password);
			LoginInfo info =  ServiceFactory.getLoginService(request).loginAndReturnPasswordType(cred);
			user = info.getUser();
			if(user!=null){
				UMTContext.saveUser(request.getSession(), info);
				IAccountService logService=(IAccountService)ServiceFactory.getBean(request, IAccountService.BEAN_ID);
				logService.login(bean.getClientName(),null , user.getId(), RequestUtil.getRemoteIP(request), new Date(),request.getHeader("User-Agent"), "");
			}
		}else{
			userLogin = getLoginInfoFromCookieAndSession(request);
			user = userLogin.getUser();
		}
		return user==null?null:user.getUserPrincipal();
	}
	
	
	/**
	 * 前端scope显示类型
	 * @param bean
	 * @return
	 */
	private List<OauthScopeBean> dealScope(String clientId,Set<String> userScope) {
		OauthClientBean client = oauthClientServer.findByClientId(clientId);
		List<OauthScopeBean> result = new ArrayList<OauthScopeBean>();
		Set<String> allScope = client.getScopeSet();
		for(String s: allScope){
			OauthScopeBean scope = new OauthScopeBean();
			scope.setId(s);
			scope.setName(s);
			if(userScope.contains(s)){
				scope.setStatus("ckecked");
			}else{
				scope.setStatus("unckecked");
			}
			result.add(scope);
		}
		return result;
	}

	private String tansferScope(Set<String> scopes){
		StringBuilder sb = new StringBuilder();
		for(String s : scopes){
			sb.append(s).append(",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	private String tansferScope(String[] scopes){
		if(scopes==null||scopes.length==0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for(String s : scopes){
			sb.append(s).append(",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	/**
	 * 用户中途取消授权
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void cancelAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException {
		OAuthAuthzRequestWrap  oauthRequest = new OAuthAuthzRequestWrap(request);
		dealAppError("access_denied","用户取消授权" ,oauthRequest.getRedirectURI(), response);
	}
	
	public void checkLogin(HttpServletRequest request, HttpServletResponse response) {
		LoginInfo loginInfo = getLoginInfoFromCookieAndSession(request);
		JSONObject obj = new JSONObject();
		if(loginInfo.getUser()!=null){
			obj.put("status", true);
			obj.put("userName", loginInfo.getUser().getCstnetId());
			obj.put("logoutURL", ServiceFactory.getWebUrl(request)+"/logout?WebServerURL=");
		}else{
			obj.put("status", false);
		}
		writeJSONResponse(response, obj);
	}
	
	private IOauthClientService getClientServer(){
		if(oauthClientServer ==null){
			initClientServer();
		}
		return oauthClientServer;
	}
	
	private synchronized void initClientServer() {
		if(oauthClientServer ==null){
			oauthClientServer = (IOauthClientService)getBeanFactory().getBean(IOauthClientService.BEAN_ID);
		}
	}
	private synchronized IAuthorizationCodeServer getCodeServer(){
		if(authorizationCodeServer==null){
			initCodeServer();
		}
		return authorizationCodeServer;
	}
	
	private synchronized void initCodeServer() {
		if(authorizationCodeServer==null){
			authorizationCodeServer = (IAuthorizationCodeServer)getBeanFactory().getBean(IAuthorizationCodeServer.BEAN_ID);
		}
	}

	private synchronized IOauthTokenService getTokenServer(){
		if(oauthTokenServer==null){
			initTokenServer();
		}
		return oauthTokenServer;
	}
	private synchronized void initTokenServer() {
		if(oauthTokenServer==null){
			oauthTokenServer = (IOauthTokenService)getBeanFactory().getBean(IOauthTokenService.BEAN_ID);
		}
	}
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	
	private void dealOAuthSystemError(String redirectURI,OAuthSystemException e,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		if(StringUtils.isEmpty(redirectURI)){
			request.setAttribute("client_id", request.getParameter("client_id"));
			request.setAttribute("errorCode", "server_error");
			request.setAttribute("errorDescription", e.getMessage());
			dealClientRedirectError(request, response);
			return;
		}
		OAuthResponse resp = null;
		try {
			resp = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND).setError("server_error")
					.location(redirectURI).buildQueryMessage();
		} catch (OAuthSystemException ex) {
			LOG.error("redirectURI="+redirectURI,ex);
		}
		LOG.error("",e);
		response.sendRedirect(resp.getLocationUri());
	}
	
	private void dealClientRedirectError(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/oauth/redirecturlerror.jsp").forward(request, response);
	}
	
}