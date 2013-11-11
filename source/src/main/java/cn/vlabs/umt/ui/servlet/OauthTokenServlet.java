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
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.oauth.as.issuer.MD5Generator;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuer;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuerImpl;
import cn.vlabs.umt.oauth.as.request.OAuthTokenRequest;
import cn.vlabs.umt.oauth.as.response.OAuthASResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
import cn.vlabs.umt.oauth.common.message.OAuthResponse;
import cn.vlabs.umt.services.user.bean.AuthorizationCodeBean;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAuthorizationCodeServer;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.IOauthTokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

public class OauthTokenServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4966747416478941162L;

	private static final Logger LOG = Logger.getLogger(OauthTokenServlet.class);
	
	private static String accessTokenTimeout = "5d";
	private static String refreshTokenTimeout = "10d";
	
	private IAuthorizationCodeServer authorizationCodeServer;
	private IOauthTokenService oauthTokenServer;
	private IOauthClientService oauthClientServer;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OAuthTokenRequest oauthRequest = null;
		response.setCharacterEncoding("utf-8");
		String redirectURI = null;
		try {
			response.setContentType("application/json");
			if("validate_access_token".equals(request.getParameter("grant_type"))){
				dealValidateAccessToken(request, response);
				return;
			}
			oauthRequest = new OAuthTokenRequest(request);
			String grantType = oauthRequest.getGrantType();
			redirectURI = oauthRequest.getRedirectURI();
			if(!validateScope(oauthRequest, response)){
				return;
			}
			if(!validateRedirectUri(oauthRequest)){
				return;
			}
			if(!validateSecret(oauthRequest,response)){
				return;
			}
			if("authorization_code".equals(grantType)){
				String code = oauthRequest.getCode();
				if(StringUtils.isEmpty(code)){
					dealAppError("invalid_request","缺乏必要的code参数" ,oauthRequest.getRedirectURI(), response);
				}else{
					dealAuthorizationCodeType(request, response, oauthRequest);
				}
			}else if("refresh_token".equals(grantType)){
				dealRefreshTokenType(request, response, oauthRequest);
			}else if("password".equals(grantType)){
				dealPasswordGrantType(oauthRequest,request,response);
			}else{
				//不被授权服务器所支持
				dealAppError("unsupported_grant_type",grantType+"此类型不被服务器支持" ,oauthRequest.getRedirectURI(), response);
			}
		} catch (OAuthSystemException ex) {
			dealOAuthSystemError(redirectURI, ex, response);
		} catch (OAuthProblemException ex) {
			dealOAuthProblemError(redirectURI, ex, response);
		}
	}
	
	/**
	 * 处理请求类型为refresh_code类型 
	 * @param request
	 * @param response
	 * @param oauthRequest
	 * @param oauthIssuerImpl
	 * @throws OAuthSystemException
	 * @throws IOException
	 */
	private void dealRefreshTokenType(HttpServletRequest request, HttpServletResponse response,
			OAuthTokenRequest oauthRequest) throws OAuthSystemException, IOException {
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		String refreshToken = oauthRequest.getRefreshToken();
		if (StringUtils.isNotEmpty(refreshToken)) {
			OauthToken token = getTokenServer().getTokenByRefresh(refreshToken);
			if (token != null && !token.isRefreshExpired()) {
				getTokenServer().delete(token);
				String accessToken = oauthIssuerImpl.accessToken();
				String newRefreshToken = oauthIssuerImpl.refreshToken();
				OauthToken newToken = getNowToken(token);
				newToken.setAccessToken(accessToken);
				newToken.setRefreshToken(newRefreshToken);
				getTokenServer().save(newToken);
				User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
				LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
				long accessTime = tansferTime(accessTokenTimeout);
				OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken)
						.setExpiresIn(String.valueOf(accessTime / 1000))
						.setParam("userInfo", getUserAsJSON(loginInfo,user,""))
						.setRefreshToken(newRefreshToken).buildJSONMessage();
				writeResponseMessage(response, r);
			}else{
				String message = "refresh_token无效或者已过期";
				dealAppError("invalid_client",message, oauthRequest.getRedirectURI(), response);
			}
		} else {
			String message = "在refresh_token提供的refresh_token不能为空";
			dealAppError("invalid_request", message,oauthRequest.getRedirectURI(), response);
		}
	}

	/**
	 * 处理类型为authorization_cod类型请求
	 * @param request
	 * @param response
	 * @param oauthRequesto
	 * @throws OAuthSystemException
	 * @throws IOException
	 */
	private void dealAuthorizationCodeType(HttpServletRequest request, HttpServletResponse response,
			OAuthTokenRequest oauthRequest) throws OAuthSystemException, IOException {
		String code = oauthRequest.getCode();
		AuthorizationCodeBean bean = getCodeServer().getByCode(code);
		
		if(bean!=null&&!bean.isExpired()){
			OauthToken token = createToken(bean,request);
			long accessTime = tansferTime(accessTokenTimeout);
			User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
			LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
			OAuthResponse r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf(accessTime/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,bean.getPasswordType()))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			getTokenServer().save(token);
			getCodeServer().deleteByCode(code);
			writeResponseMessage(response, r);
		}else{
			dealAppError("invalid_grant","提供的code["+bean+"]无效或者已过期", oauthRequest.getRedirectURI(), response);
		}
	}
	
	private void dealPasswordGrantType(OAuthTokenRequest oauthRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = oauthRequest.getUsername();
		String password = oauthRequest.getPassword();
		if(StringUtils.isEmpty(userName)){
			dealAppError("invalid_request","缺乏必要的username参数" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		if(StringUtils.isEmpty(password)){
			dealAppError("invalid_request","缺乏必要的password参数" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		OauthClientBean bean = getClientServer().findByClientId(oauthRequest.getClientId());
		if(!OauthClientBean.APP_TYPE_PHONE_APP.equals(bean.getAppType())){
			dealAppError("invalid_request","应用类型非移动类型" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		UsernamePasswordCredential cred = new UsernamePasswordCredential(userName,password);
		LoginInfo info =  ServiceFactory.getLoginService(request).loginAndReturnPasswordType(cred);
		if(info.getUser()==null){
			dealAppError("invalid_grant","用户名或密码校验错误" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		String uid = ServiceFactory.getUserService(request).getUserByLoginName(info.getUserPrincipal().getName()).getId()+"";
		String redirectURI = oauthRequest.getRedirectURI();
		try {
			OauthToken token = createToken(oauthRequest.getClientId(), redirectURI, request, tansferScope(oauthRequest.getScopes()), uid);
			long accessTime = tansferTime(accessTokenTimeout);
			User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
			LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
			OAuthResponse r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf(accessTime/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,info.getPasswordType()))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			getTokenServer().save(token);
			writeResponseMessage(response, r);
		} catch (OAuthSystemException e) {
			dealOAuthSystemError(redirectURI, e, response);
		}
	}
	/**
	 * 处理资源服务器accessToken认证，这个认证服务由umt自行定义
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void dealValidateAccessToken(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String accessToken = request.getParameter("access_token");
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		if(StringUtils.isEmpty(clientId)){
			dealAppError("invalid_grant", "client_id为空", "", response);
		}
		OauthClientBean client = getClientServer().findByClientId(clientId);
		if(client==null||!StringUtils.equals(clientSecret, client.getClientSecret())){
			dealAppError("invalid_grant", "client_id不存在或client_secret错误", "", response);
		}
		OauthToken token = getTokenServer().getTokenByAccess(accessToken);
		if(token==null){
			dealAppError("invalid_grant", "access_token["+accessToken+"]不存在", "", response);
		}
		if(token.isAccessExpired()){
			dealAppError("invalid_grant", "access_token["+accessToken+"]已过期", "", response);
		}
		User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
		LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
		OAuthResponse r;
		try {
			r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf((token.getAccessExpired().getTime()-System.currentTimeMillis())/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,null))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			writeResponseMessage(response, r);
		} catch (OAuthSystemException e) {
			dealAppError("server_error", "服务器错误", "", response);
		}
		
		
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

	/**
	 * 校验client_secret
	 * @param oauthRequest
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	private boolean validateSecret(OAuthTokenRequest oauthRequest, HttpServletResponse response) throws IOException {
		String clientId = oauthRequest.getClientId();
		OauthClientBean bean = getClientServer().findByClientId(clientId);
		if(bean!=null){
			String secret = oauthRequest.getClientSecret();
			if(StringUtils.isEmpty(secret)&&StringUtils.isEmpty(bean.getClientSecret())){
				return true;
			}
			if(secret.equals(bean.getClientSecret())){
				return true;
			}
		}
		dealAppError("invalid_grant", "client_secret提供错误",oauthRequest.getRedirectURI(), response);
		return false;
	}
	/**
	 * 校验client_redirect_uri
	 * @param oauthRequest
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private boolean validateRedirectUri(OAuthTokenRequest oauthRequest)throws IOException{
		String clientId = oauthRequest.getClientId();
		OauthClientBean bean = getClientServer().findByClientId(clientId);
		if(bean!=null){
			String redirectUri = oauthRequest.getRedirectURI();
			if(!bean.getRedirectURI().equals(redirectUri)){
				LOG.error("client id "+bean.getClientId()+";client name"+bean.getClientName()+"client redierect_url "+bean.getRedirectURI()+" not match;now redirect_uri "+redirectUri);
			}
		}
		
		return true;
	}
	/**
	 * 校验请求的scope时候超出client的范围
	 * @param oauthRequest
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private boolean validateScope(OAuthTokenRequest oauthRequest,HttpServletResponse response) throws IOException{
		Set<String> scope = oauthRequest.getScopes();
		if(scope==null||scope.isEmpty()){
			return true;
		}else{
			String clientId = oauthRequest.getClientId();
			OauthClientBean bean = getClientServer().findByClientId(clientId);
			if(bean.validateScope(scope)){
				return true;
			}else{
				dealAppError("invalid_scope", "申请的scope错误",oauthRequest.getRedirectURI(), response);
				return false;
			}
		}
	}

	private void writeResponseMessage(HttpServletResponse response, OAuthResponse r) throws IOException {
		response.setHeader("Cache-Control", "no-store");
		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
	}
	
	private void dealAppError(String errorCode,String desc,String redirectURI,HttpServletResponse response) throws IOException{
		OAuthResponse resp = null;
		try {
			resp = OAuthASResponse
					.errorResponse(400).setError(errorCode).setErrorDescription(desc)
					.location(redirectURI).buildJSONMessage();
			response.setStatus(resp.getResponseStatus());
			PrintWriter pw = response.getWriter();
			pw.print(resp.getBody());
			pw.flush();
	        pw.close();
		} catch (OAuthSystemException e) {
			dealOAuthSystemError(redirectURI, e, response);
		}
	}
	private void dealOAuthSystemError(String redirectURI,OAuthSystemException e,HttpServletResponse response) throws IOException{
		try {
			OAuthResponse resp = OAuthASResponse
					.errorResponse(400)
					.setError("server_error")
					.setErrorDescription(e.getMessage())
					.location(redirectURI)
					.buildJSONMessage();
			response.setStatus(resp.getResponseStatus());
			PrintWriter pw = response.getWriter();
			pw.print(resp.getBody());
			pw.flush();
	        pw.close();
		} catch (OAuthSystemException ex) {
			LOG.error("redirectURI="+redirectURI,ex);
			response.setStatus(400);
			JSONObject obj = new JSONObject();
			obj.put("error", "server_error");
			obj.put("error_description", ex.getMessage());
			PrintWriter pw = response.getWriter();
			pw.print(obj.toString());
			pw.flush();
	        pw.close();
		}
		LOG.error("",e);
	}
	private void dealOAuthProblemError(String redirectURI,OAuthProblemException e,HttpServletResponse response) throws IOException{
		OAuthResponse resp = null;
		try {
			resp = OAuthASResponse
					.errorResponse(400)
					.setError("server_error")
					.error(e)
					.location(redirectURI).buildJSONMessage();
			response.setStatus(resp.getResponseStatus());
			PrintWriter pw = response.getWriter();
			pw.print(resp.getBody());
			pw.flush();
	        pw.close();
	        response.sendError(400);
		} catch (OAuthSystemException ex) {
			LOG.error("redirectURI="+redirectURI,ex);
			dealOAuthSystemError(redirectURI, ex, response);
		}
		LOG.error("",e);
	}
	
	
	private OauthToken createToken(AuthorizationCodeBean bean,HttpServletRequest request)
			throws OAuthSystemException {
		String uid = bean.getUid()+"";
		String scopes = bean.getScope();
		return createToken(bean.getClientId(),bean.getRedirectURI(), request, scopes,uid);
	}

	public static OauthToken createToken(String clientId,String redirectURI, HttpServletRequest request,
			String scopes,String uid) throws OAuthSystemException {
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		String accessToken = oauthIssuerImpl.accessToken();
		String refreshToken = oauthIssuerImpl.refreshToken();
		OauthToken token = new OauthToken();
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		token.setCreateTime(new Date());
		token.setClientId(clientId);
		token.setAccessExpired(new Date(System.currentTimeMillis()+tansferTime(accessTokenTimeout)));
		token.setRefreshExpired(new Date(System.currentTimeMillis()+tansferTime(refreshTokenTimeout)));
		token.setRedirectURI(redirectURI);
		token.setScope(scopes);
		token.setUid(uid);
		return token;
	}
	
	public static String getUserAsJSON(LoginNameInfo info,User user,String passwordType){
		JSONObject object = new JSONObject();
		object.put("umtId", user.getUmtId());
		putStringToJSON(object, "truename", user.getTrueName());
		putStringToJSON(object, "type", user.getType());
		putStringToJSON(object, "cstnetId", user.getCstnetId());
		putStringToJSON(object, "cstnetIdStatus", info.getStatus());
		putStringToJSON(object, "securityEmail", user.getSecurityEmail());
		putStringToJSON(object, "passwordType",passwordType);
		String []emails = user.getSecondaryEmails();
		if(emails!=null&&emails.length>0){
			JSONArray ar = new JSONArray();
			for(int i=0;i<emails.length;i++){
				ar.add(emails[i]);
			}
			object.put("secondaryEmails", ar);
		}
		return object.toJSONString();
	}
	private static void putStringToJSON(JSONObject obj,String key,String value){
		if(StringUtils.isNotEmpty(value)){
			obj.put(key, value);
		}
	}
	private OauthToken getNowToken(OauthToken oldToken){
		OauthToken newToken = new OauthToken();
		newToken.setCreateTime(new Date());
		newToken.setAccessExpired(new Date(System.currentTimeMillis()+tansferTime(accessTokenTimeout)));
		newToken.setRefreshExpired(new Date(System.currentTimeMillis()+tansferTime(refreshTokenTimeout)));
		newToken.setClientId(oldToken.getClientId());
		newToken.setRedirectURI(oldToken.getRedirectURI());
		newToken.setScope(oldToken.getScope());
		newToken.setUid(oldToken.getUid());
		return newToken;
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

	private synchronized IOauthClientService getClientServer(){
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

	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	
	private static long tansferTime(String time){
		Character end = time.charAt(time.length()-1);
		if("d".equalsIgnoreCase(end.toString())){
			return 24l*3600l*1000l*Long.parseLong((time.substring(0, time.length()-1)));
		}else if("h".equalsIgnoreCase(end.toString())){
			return 3600l*1000l*Long.parseLong(time.substring(0, time.length()-1));
		}else{
			return 24l*3600l*1000l*Long.parseLong(time.substring(0, time.length()));
		}
	}
}