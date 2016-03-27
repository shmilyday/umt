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
package cn.vlabs.umt.oauth.rsclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.Oauth;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.client.HttpClient;
import cn.vlabs.umt.oauth.client.HttpsConnectionClient;
import cn.vlabs.umt.oauth.client.OAuthClient;
import cn.vlabs.umt.oauth.client.URLConnectionClient;
import cn.vlabs.umt.oauth.client.request.OAuthClientRequest;
import cn.vlabs.umt.oauth.client.response.OAuthJSONAccessTokenResponse;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;

public class RsClient {
	private static final String ACCESS_TOKEN = "access_token";
	private String clientId;
	private String clientSecret;
	private String accessTokenURL;
	
	public RsClient(String name) throws IOException {
		InputStream in = Oauth.class.getResourceAsStream("/"+name);
		Properties p = new Properties();
		p.load(in);
		loadProperty(p);
		in.close();
	}
	
	public RsClient(Properties p) {
		loadProperty(p);
	}
	
	private void loadProperty(Properties p) {
		this.clientId = p.getProperty("client_id");
		this.clientSecret = p.getProperty("client_secret");
		this.accessTokenURL = p.getProperty("access_token_URL");
	}
	
	public AccessToken validateAccessToken(String accessToken) throws OAuthProblemException, UMTOauthConnectException{
		if(accessToken==null||accessToken.length()==0){
			throw OAuthProblemException.error("invalid_request","access_token值为空");
		}
		try {
			OAuthClientRequest request = OAuthClientRequest.tokenLocation(accessTokenURL)
															.setClientId(clientId)
															.setClientSecret(clientSecret)
															.buildBodyMessage();
			Map<String,String> params = new HashMap<String,String>();
			params.put("grant_type", "validate_access_token");
			params.put(ACCESS_TOKEN, accessToken);
			request.setBody(request.getBody()+"&"+formatForm(params));
			OAuthClient oAuthClient = new OAuthClient(getHttpClient(accessTokenURL));
			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request,
					OAuthJSONAccessTokenResponse.class);
			AccessToken token = new AccessToken();
			token.setAccessToken(oAuthResponse.getAccessToken());
			token.setRefreshToken(oAuthResponse.getRefreshToken());
			token.setExpiresIn(oAuthResponse.getExpiresIn());
			token.setScope(oAuthResponse.getScope());
			token.setUserInfo(getUserInfo(oAuthResponse.getParam("userInfo")));
			return token;
		} catch (OAuthSystemException e) {
			throw new UMTOauthConnectException("获取accessToken错误", e);
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
	private HttpClient getHttpClient(String accessTokenURL){
		if(accessTokenURL.toLowerCase().startsWith("https")){
			return new HttpsConnectionClient();
		}else{
			return new URLConnectionClient();
		}
	}
	
	private String formatForm(Map<String,String> map){
		if(map==null||map.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String tmp=null;
		for(Map.Entry<String, String> entry:map.entrySet()){
			String key = entry.getKey();
			if(key!=null&&!"".equals(key)){
				tmp = entry.getKey()+"="+entry.getValue();
				try {
					tmp = URLEncoder.encode(entry.getKey(), "UTF-8")+"="+URLEncoder.encode(entry.getValue(), "UTF-8");
					sb.append(tmp);
					sb.append("&");
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}