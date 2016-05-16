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
package cn.vlabs.umt.services.user.bean;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class OauthToken implements Serializable {
	private static final long serialVersionUID = -7297480492302488426L;
	private int id;
	private String accessToken;
	private String refreshToken;
	private Date createTime;
	private Date accessExpired;
	private Date refreshExpired;
	private String uid;
	private String scope;
	private String clientId;
	private String redirectURI;
	private String passwordType;
	@Override
	public String toString() {
		return toJson().toString();
	}
	public JSONObject toJson(){
		JSONObject object=new JSONObject();
		try {
			object.put("id", id);
			object.put("accessToken", accessToken);
			object.put("refreshToken", refreshToken);
			object.put("createTime", createTime);
			object.put("accessExpired", accessExpired);
			object.put("refreshExpired", refreshExpired);
			object.put("uid", uid);
			object.put("scope", scope);
			object.put("clientId", clientId);
			object.put("redirectURI", redirectURI);
			object.put("passwordType", passwordType);
			return object;
		} catch (JSONException e) {
			return new JSONObject();
		}
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getAccessExpired() {
		return accessExpired;
	}
	public void setAccessExpired(Date accessExpired) {
		this.accessExpired = accessExpired;
	}
	public Date getRefreshExpired() {
		return refreshExpired;
	}
	public void setRefreshExpired(Date refreshExpired) {
		this.refreshExpired = refreshExpired;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	public boolean isRefreshExpired() {
		return System.currentTimeMillis()>refreshExpired.getTime();
	}
	public boolean isAccessExpired() {
		return System.currentTimeMillis()>accessExpired.getTime();
	}
	public String getPasswordType() {
		return passwordType;
	}
	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}
	
}