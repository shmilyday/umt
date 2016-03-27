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
package cn.vlabs.umt.services.auth;

import cn.vlabs.umt.oauth.Oauth;


public class ThirdPartyAuth {
	private String code;
	private String name;
	private String clientId;
	private String secret;
	private String serverUrl;
	private String baseUrl;
	private String theme;
	private boolean showInLogin;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}
	public Oauth createOauth(){
		Oauth oauth= new Oauth(getServerUrl(),baseUrl+"/callback/"+code,clientId,secret,getTheme());
		return oauth;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getTheme() {
		if (theme==null){
			return "full";
		}else{
			return theme;
		}
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public boolean isShowInLogin() {
		return showInLogin;
	}
	public void setShowInLogin(boolean showInLogin) {
		this.showInLogin = showInLogin;
	}
}
