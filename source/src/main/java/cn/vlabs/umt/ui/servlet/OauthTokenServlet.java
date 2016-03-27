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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.falcon.api.cache.ICacheService;
import net.duckling.vmt.api.IRestOrgService;
import net.duckling.vmt.api.domain.VmtOrg;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.rest.ServiceException;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.OauthLog;
import cn.vlabs.umt.oauth.as.issuer.MD5Generator;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuer;
import cn.vlabs.umt.oauth.as.issuer.OAuthIssuerImpl;
import cn.vlabs.umt.oauth.as.request.OAuthTokenRequest;
import cn.vlabs.umt.oauth.as.response.OAuthASResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
import cn.vlabs.umt.oauth.common.message.OAuthResponse;
import cn.vlabs.umt.services.account.IOauthLogService;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.AuthorizationCodeBean;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.OauthCredential;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAuthorizationCodeServer;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.IOauthTokenService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

@Controller
@RequestMapping(value={"/oauth2/token","/ouath2/token"})
//将错就错。。。
public class OauthTokenServlet{

	private static final Logger LOG = Logger.getLogger(OauthTokenServlet.class);
	
	private static String accessTokenTimeout = "5d";
	private static String refreshTokenTimeout = "10d";
	@Autowired
	private IAuthorizationCodeServer authorizationCodeServer;
	@Autowired
	private IOauthTokenService oauthTokenServer;
	@Autowired
	private IOauthClientService oauthClientServer;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private IOauthLogService oauthLogService;
	@Autowired
	private IRestOrgService orgService;
	
	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			OauthClientBean bean = oauthClientServer.findByClientId(oauthRequest.getClientId());
			OauthLog log=new OauthLog();
			log.setAction(OauthLog.ACTION_VALIDATE_CODE);
			log.setClientId(oauthRequest.getClientId());
			log.setIp(RequestUtil.getRemoteIP(request));
			log.setUserAgent(request.getHeader("User-Agent"));
			log.setClientName(bean.getClientName());
			if(!validateScope(oauthRequest, response)){
				log.setResult(OauthLog.RESULT_SCOPE_MISMATCH);
				log.setAssertDesc(oauthRequest.getScopes().toString(),"["+bean.getScope()+"]");
				return;
			}
			if(!validateRedirectUri(oauthRequest,bean)){
				log.setResult(OauthLog.RESULT_REDIRECT_URL_MISMATCH);
				log.setAssertDesc(bean.getRedirectURI(),oauthRequest.getRedirectURI());
				oauthLogService.addLog(log);
				return;
			}
			if(!validateSecret(oauthRequest,response)){
				log.setResult(OauthLog.RESULT_SECRET_ERROR);
				log.setAssertDesc(bean.getClientSecret(),oauthRequest.getClientSecret());
				oauthLogService.addLog(log);
				return;
			}
			if("authorization_code".equals(grantType)){
				String code = oauthRequest.getCode();
				if(StringUtils.isEmpty(code)){
					log.setResult(OauthLog.RESULT_CODE_NOT_FOUND);
					oauthLogService.addLog(log);
					dealAppError("invalid_request","缺乏必要的code参数" ,oauthRequest.getRedirectURI(), response);
				}else{
					dealAuthorizationCodeType(request, response, oauthRequest,bean);
				}
			}else if("refresh_token".equals(grantType)){
				dealRefreshTokenType(request, response, oauthRequest,bean);
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
			OAuthTokenRequest oauthRequest,OauthClientBean bean) throws OAuthSystemException, IOException {
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		String refreshToken = oauthRequest.getRefreshToken();
		OauthLog log=new OauthLog();
		log.setAction(OauthLog.ACTION_VALIDATE_REFRESH_ACCESS_TOKEN);
		log.setClientId(bean==null?null:bean.getClientId());
		log.setClientName(bean==null?null:bean.getClientName());
		log.setIp(RequestUtil.getRemoteIP(request));
		log.setUserAgent(request.getHeader("User-Agent"));
		if (StringUtils.isNotEmpty(refreshToken)) {
			OauthToken token = oauthTokenServer.getTokenByRefresh(refreshToken);
			if (token != null && !token.isRefreshExpired()) {
				oauthTokenServer.delete(token);
				String accessToken = oauthIssuerImpl.accessToken();
				String newRefreshToken = oauthIssuerImpl.refreshToken();
				OauthToken newToken = getNowToken(token);
				newToken.setAccessToken(accessToken);
				newToken.setRefreshToken(newRefreshToken);
				oauthTokenServer.save(newToken);
				User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
				LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
				long accessTime = tansferTime(accessTokenTimeout);
				OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken)
						.setExpiresIn(String.valueOf(accessTime / 1000))
						.setParam("userInfo", getUserAsJSON(loginInfo,user,"",getEncPwd(oauthRequest.getCode()),bean.isNeedOrgInfo(),orgService))
						.setRefreshToken(newRefreshToken)
						.setScope(token.getScope()).buildJSONMessage();
				writeResponseMessage(response, r);
				log.setResult(OauthLog.RESULT_SUCCESS);
			}else{
				String message = "refresh_token无效或者已过期";
				dealAppError("invalid_client",message, oauthRequest.getRedirectURI(), response);
				log.setResult(OauthLog.RESULT_REFRESH_TOKEN_EXPIRED);
			}
		} else {
			String message = "在refresh_token提供的refresh_token不能为空";
			dealAppError("invalid_request", message,oauthRequest.getRedirectURI(), response);
			log.setResult(OauthLog.RESULT_REFRESH_TOKEN_REQUIRED);
		}
		oauthLogService.addLog(log);
	}
	private String getEncPwd(String code){
		return (String)cacheService.get("pwd.enc."+code);
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
			OAuthTokenRequest oauthRequest,OauthClientBean clientBean) throws OAuthSystemException, IOException {
		String code = oauthRequest.getCode();
		AuthorizationCodeBean bean = authorizationCodeServer.getByCode(code);
		OauthLog log=new OauthLog();
		log.setAction(OauthLog.ACTION_VALIDATE_CODE);
		log.setClientId(oauthRequest.getClientId());
		log.setIp(RequestUtil.getRemoteIP(request));
		log.setUserAgent(request.getHeader("User-Agent"));
		log.setClientName(clientBean.getClientName());
		if(bean!=null&&!bean.isExpired()){
			OauthToken token = createToken(bean,request);
			long accessTime = tansferTime(accessTokenTimeout);
			User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
			LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
			OAuthResponse r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf(accessTime/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,bean.getPasswordType(),getEncPwd(oauthRequest.getCode()),clientBean.isNeedOrgInfo(),orgService))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			oauthTokenServer.save(token);
			authorizationCodeServer.deleteByCode(code);
			log.setResult(OauthLog.RESULT_SUCCESS);
			log.setUid(user.getId());
			log.setCstnetId(user.getCstnetId());
			oauthLogService.addLog(log);
			writeResponseMessage(response, r);
		}else{
			log.setResult(OauthLog.RESULT_CODE_EXPIRED);
			log.setDesc(code);
			oauthLogService.addLog(log);
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
		OauthClientBean bean = oauthClientServer.findByClientId(oauthRequest.getClientId());
		if(!OauthClientBean.APP_TYPE_PHONE_APP.equals(bean.getAppType())){
			dealAppError("invalid_request","应用类型非移动类型" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		UsernamePasswordCredential cred = new UsernamePasswordCredential(userName,password);
		LoginService ls=ServiceFactory.getLoginService(request);
		LoginInfo info =  ls.loginAndReturnPasswordType(cred);
		if(info.getUser()==null){
			info=ls.loginAndReturnPasswordType(new OauthCredential(oauthRequest.getClientId(), userName, password));
		}
		if(info.getUser()==null){
			dealAppError("invalid_grant","用户名或密码校验错误" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		String uid = info.getUser().getId()+"";
		String redirectURI = oauthRequest.getRedirectURI();
		if(CommonUtils.isNull(redirectURI)){
			dealAppError("invalid_grant","redirectUrl不能为空" ,oauthRequest.getRedirectURI(), response);
			return;
		}
		try {
			OauthToken token = createToken(oauthRequest.getClientId(), redirectURI, request, tansferScope(oauthRequest.getScopes()), uid,info.getPasswordType() );
			
			long accessTime = tansferTime(accessTokenTimeout);
			User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
			LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
			OAuthResponse r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf(accessTime/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,info.getPasswordType(),null,bean.isNeedOrgInfo(),orgService))
					.setRefreshToken(token.getRefreshToken())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			oauthTokenServer.save(token);
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
			return;
		}
		OauthClientBean client = oauthClientServer.findByClientId(clientId);
		
		
		if(client==null){
			dealAppError("invalid_grant", "client_id不存在或client_secret错误", "", response);
			return;
		}
		OauthLog log=new OauthLog();
		log.setAction(OauthLog.ACTION_VALIDATE_ACCESS_TOKEN);
		log.setClientId(client==null?null:client.getClientId());
		log.setClientName(client==null?null:client.getClientName());
		log.setIp(RequestUtil.getRemoteIP(request));
		log.setUserAgent(request.getHeader("User-Agent"));
		if(!StringUtils.equals(clientSecret, client.getClientSecret())){
			log.setAssertDesc(client==null?null:client.getClientSecret(), clientSecret);
			log.setResult(OauthLog.RESULT_SECRET_ERROR);
			oauthLogService.addLog(log);
			dealAppError("invalid_grant", "client_id不存在或client_secret错误", "", response);
			return;
		}
		OauthToken token = oauthTokenServer.getTokenByAccess(accessToken);
		if(token==null){
			log.setDesc(accessToken);
			log.setResult(OauthLog.RESULT_TOKEN_NOT_FOUND);
			oauthLogService.addLog(log);
			dealAppError("invalid_grant", "access_token["+accessToken+"]不存在", "", response);
			return;
		}
		if(token.isAccessExpired()){
			log.setDesc(accessToken);
			log.setResult(OauthLog.RESULT_TOKEN_EXPIRED);
			oauthLogService.addLog(log);
			dealAppError("invalid_grant", "access_token["+accessToken+"]已过期", "", response);
			return;
		}
		
		if(!StringUtils.equals(token.getClientId(), client.getClientId())){
			LOG.error("mismatch_accessToken_clientId  access_token["+accessToken+"] clientId["+client.getClientId()+"]");
		}
		
		User user = ServiceFactory.getUserService(request).getUserByUid(Integer.parseInt(token.getUid()));
		LoginNameInfo loginInfo = ServiceFactory.getLoginNameService(request).getALoginNameInfo(user.getId(), user.getCstnetId());
		OAuthResponse r;
		try {
			r = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token.getAccessToken())
					.setExpiresIn(String.valueOf((token.getAccessExpired().getTime()-System.currentTimeMillis())/1000))
					.setParam("userInfo", getUserAsJSON(loginInfo,user,null,null,client.isNeedOrgInfo(),orgService))
					.setRefreshToken(token.getRefreshToken())
					.setScope(token.getScope())
					.buildJSONMessage();
			response.setStatus(r.getResponseStatus());
			writeResponseMessage(response, r);
			log.setResult(OauthLog.RESULT_SUCCESS);
			log.setCstnetId(user.getCstnetId());
			log.setUid(user.getId());
			oauthLogService.addLog(log);
			
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
		OauthClientBean bean = oauthClientServer.findByClientId(clientId);
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
	private boolean validateRedirectUri(OAuthTokenRequest oauthRequest,OauthClientBean bean)throws IOException{
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
			OauthClientBean bean = oauthClientServer.findByClientId(clientId);
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
		return createToken(bean.getClientId(),bean.getRedirectURI(), request, scopes,uid, bean.getPasswordType());
	}

	public static OauthToken createToken(String clientId,String redirectURI, HttpServletRequest request,
			String scopes,String uid, String passwordType) throws OAuthSystemException {
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
		token.setPasswordType(passwordType);
		return token;
	}
	
	public static String getUserAsJSON(LoginNameInfo info,User user,String passwordType,String encPassword,boolean getOrgInfo,IRestOrgService orgService ){
		JSONObject object = new JSONObject();
		object.put("umtId", user.getUmtId());
		putStringToJSON(object, "truename", user.getTrueName());
		putStringToJSON(object, "type", user.getType());
		putStringToJSON(object, "cstnetId", user.getCstnetId().toLowerCase());
		putStringToJSON(object, "cstnetIdStatus", info.getStatus());
		//putStringToJSON(object, "securityEmail", CommonUtils.isNull(user.getSecurityEmail())?null:user.getSecurityEmail().toLowerCase());
		putStringToJSON(object, "passwordType",passwordType);
		putStringToJSON(object, "encPassword",encPassword);
		if(getOrgInfo){
			try {
				List<VmtOrg> orgs=orgService.getSbOrg(user.getUmtId());
				
				if(!CommonUtils.isNull(orgs)){
					JSONArray orgsJson=new JSONArray();
					for(VmtOrg org:orgs){
						JSONObject orgJson=new JSONObject();
						orgJson.put("orgName", org.getName());
						orgJson.put("orgId", org.getSymbol());
						orgJson.put("isCas", org.isCas());
						orgJson.put("isCoreMail", org.isCoreMail());
						orgJson.put("orgType", org.getType());
						putArrayToJSON(orgJson, "domains", org.getDomain());
						orgsJson.add(orgJson);
					}
					object.put("orgInfo", orgsJson);
				}
			} catch (ServiceException e) {
				LOG.error(e);
			}
		}
		//putArrayToJSON(object,"secondaryEmails",user.getSecondaryEmails());
		return object.toJSONString();
	}
	private static void putArrayToJSON(JSONObject obj,String key,String[] value){
		if(value!=null&&value.length>0){
			JSONArray ar = new JSONArray();
			for(int i=0;i<value.length;i++){
				ar.add(value[i].toLowerCase());
			}
			obj.put(key, ar);
		}
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