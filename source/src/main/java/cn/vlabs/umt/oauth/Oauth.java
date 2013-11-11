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
package cn.vlabs.umt.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cn.vlabs.umt.oauth.client.HttpClient;
import cn.vlabs.umt.oauth.client.HttpsConnectionClient;
import cn.vlabs.umt.oauth.client.OAuthClient;
import cn.vlabs.umt.oauth.client.URLConnectionClient;
import cn.vlabs.umt.oauth.client.request.OAuthClientRequest;
import cn.vlabs.umt.oauth.client.response.OAuthJSONAccessTokenResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
import cn.vlabs.umt.oauth.common.message.types.GrantType;
import cn.vlabs.umt.oauth.common.message.types.ResponseType;

public class Oauth {
	private String clientId;
	private String clientSecret;
	private String redirectURI;
	private String authorizeURL;
	private String accessTokenURL;
	private String scope;
	private String theme;

	public Oauth(String name) throws IOException {
		InputStream in = Oauth.class.getResourceAsStream("/"+name);
		Properties p = new Properties();
		p.load(in);
		loadProperties(p);
		in.close();
	}
	
	public Oauth(Properties p) {
		loadProperties( p);
	}
	private void loadProperties(Properties p) {
		this.clientId = p.getProperty("client_id");
		this.clientSecret = p.getProperty("client_secret");
		this.redirectURI = p.getProperty("redirect_uri");
		this.authorizeURL = p.getProperty("authorize_URL");
		this.accessTokenURL = p.getProperty("access_token_URL");
		this.scope = p.getProperty("scope");
		this.theme=p.getProperty("theme");
	}
	public String getAuthorizeURL(ServletRequest request) throws UMTOauthConnectException {
		try {
			OAuthClientRequest req = OAuthClientRequest.authorizationLocation(authorizeURL).setClientId(clientId)
					.setRedirectURI(redirectURI).setResponseType(ResponseType.CODE.toString()).setScope(scope)
					.buildQueryMessage();
			if(theme==null||theme.length()==0){
				return req.getLocationUri();
			}else{
				return req.getLocationUri()+"&theme="+theme;
			}
		} catch (OAuthSystemException e) {
			throw new UMTOauthConnectException("获取authorization code URL错误", e);
		}
	}

	public AccessToken getAccessTokenByRequest(ServletRequest req) throws UMTOauthConnectException,OAuthProblemException {
		OAuthClientRequest request;
		String url="";
		try {

			String code = req.getParameter("code");
			if(code==null||code.length()==0){
				String error = req.getParameter("error");
				OAuthProblemException ex = null;
				if(error!=null&&error.length()>0){
					ex= OAuthProblemException.error(error,req.getParameter("error_description"));
				}else{
					ex= OAuthProblemException.error("invalid_request","code值为空");
				}
				throw ex;
			}
			request = OAuthClientRequest.tokenLocation(accessTokenURL).setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(clientId).setClientSecret(clientSecret).setRedirectURI(redirectURI).setCode(code)
					.buildBodyMessage();
			
			OAuthClient oAuthClient = new OAuthClient(getHttpClient(accessTokenURL));
			url=request.getLocationUri();
			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request,
					OAuthJSONAccessTokenResponse.class);
			String error = oAuthResponse.getParam("error");
			if(error!=null&&error.length()>0){
				OAuthProblemException ex=OAuthProblemException.error(error,oAuthResponse.getParam("error_description"));
				throw ex;
			}
			AccessToken token = new AccessToken();
			token.setAccessToken(oAuthResponse.getAccessToken());
			token.setRefreshToken(oAuthResponse.getRefreshToken());
			token.setExpiresIn(oAuthResponse.getExpiresIn());
			token.setScope(oAuthResponse.getScope());
			token.setUserInfo(getUserInfo(oAuthResponse.getParam("userInfo")));
			return token;
		} catch (OAuthSystemException e) {
			throw new UMTOauthConnectException("获取accessToken错误url"+url, e);
		} 
	}
	private HttpClient getHttpClient(String accessTokenURL){
		if(accessTokenURL.toLowerCase().startsWith("https")){
			return new HttpsConnectionClient();
		}else{
			return new URLConnectionClient();
		}
	}
	private UserInfo getUserInfo(String param) {
		if(param==null||param.length()==0){
			return null;
		}
		JSONObject obj;
		try {
			UserInfo user = new UserInfo();
			obj = new JSONObject(param);
			user.setType(getFromJSON(obj,"type"));
			user.setTrueName(getFromJSON(obj,"truename"));
			user.setCstnetId(getFromJSON(obj,"cstnetId"));
			user.setUmtId(getFromJSON(obj,"umtId"));
			user.setPasswordType(getFromJSON(obj, "passwordType"));
			user.setCstnetIdStatus(getFromJSON(obj,"cstnetIdStatus"));
			user.setSecurityEmail(getFromJSON(obj,"securityEmail"));
			try{
				JSONArray emails = obj.getJSONArray("secondaryEmails");
				if(emails!=null&&emails.length()>0){
					String[] r = new String[emails.length()];
					for(int i=0;i<emails.length();i++){
						r[i]=emails.getString(i);
					}
					user.setSecondaryEmails(r);
				}
			}catch (JSONException e) {
			}
			return user;
		} catch (JSONException e) {
			return null;
		}
	}
	
	private String getFromJSON(JSONObject obj,String key){
		try{
			return obj.getString(key);
		}catch (JSONException e) {
			return null;
		}
	}

}