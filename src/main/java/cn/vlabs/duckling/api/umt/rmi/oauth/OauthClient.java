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
package cn.vlabs.duckling.api.umt.rmi.oauth;

import java.io.Serializable;

public class OauthClient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1645090901729070762L;
	private String clientId;
	private String clientName;
	private String clientUrl;
	private String logo100Url;
	private String logo64Url;
	private String logo32Url;
	private String logo16Url;
	private String logoCustom;
	public String getLogo100Url() {
		return logo100Url;
	}
	public void setLogo100Url(String logo100Url) {
		this.logo100Url = logo100Url;
	}
	public String getLogo64Url() {
		return logo64Url;
	}
	public void setLogo64Url(String logo64Url) {
		this.logo64Url = logo64Url;
	}
	public String getLogo32Url() {
		return logo32Url;
	}
	public void setLogo32Url(String logo32Url) {
		this.logo32Url = logo32Url;
	}
	public String getLogo16Url() {
		return logo16Url;
	}
	public void setLogo16Url(String logo16Url) {
		this.logo16Url = logo16Url;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientUrl() {
		return clientUrl;
	}
	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}
	public String getLogoCustom() {
		return logoCustom;
	}
	public void setLogoCustom(String logoCustom) {
		this.logoCustom = logoCustom;
	}
	
	

}
