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

public class AppAccessPassword {
	private Object data;
	private String type;
	private String appId;
	private AppSecret secret;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public AppAccessPassword(Object data,AppSecret secret){
		if(data instanceof OauthClientBean){
			type="oauth";
			this.appId=((OauthClientBean)data).getClientId();
		}else if(data instanceof LdapBean){
			LdapBean bean = (LdapBean)data;
			type=bean.getType();
			this.appId=((LdapBean)data).getId()+"";
		}
		this.data=data;
		this.secret=secret;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public AppSecret getSecret() {
		return secret;
	}
	public void setSecret(AppSecret secret) {
		this.secret = secret;
	}
	
	
}
