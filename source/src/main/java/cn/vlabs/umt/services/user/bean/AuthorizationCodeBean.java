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
import java.util.HashSet;
import java.util.Set;

public class AuthorizationCodeBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2388059664175565054L;
	
	private String code;
	private String clientId;
	private String scope;
	private int uid;
	private Date createTime;
	private String passwordType;
	private Date expiredTime;
	private String redirectURI;
	private String state;
	
	public String getPasswordType() {
		return passwordType;
	}
	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	public Set<String> getScopeSet(){
		String[] v = scope.split(",");
		Set<String> all = new HashSet<String>();
		for(String s : v){
			all.add(s);
		}
		return all;
	}
	public void updateScope(String [] ss){
		if(ss==null||ss.length==0){
			return;
		}
		StringBuilder sb = new StringBuilder();
		for(String s :ss){
			sb.append(s).append(",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		scope = sb.toString();
	}
	public boolean isExpired() {
		return System.currentTimeMillis()>getExpiredTime().getTime();
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("client_id=").append(clientId);
		sb.append(";createTime").append(createTime);
		sb.append(";expiredTime=").append(expiredTime);
		sb.append("redirectURL=").append(redirectURI);
		sb.append(";code=").append(code);
		sb.append(";passwordType=").append(passwordType);
		sb.append(";state=").append(state);
		return sb.toString();
	}
}