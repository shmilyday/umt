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
package cn.vlabs.umt.oauth;

public class OrgInfo {
	/**
	 * 组织id
	 * */
	private String orgId;
	/**
	 * 组织名称
	 * */
	private String orgName;
	/**
	 * 域名,第一个为主域名
	 * */
	private String[] domains;
	/**
	 * 是否为院内组织
	 * */
	private boolean isCas;
	/**
	 * 是否是邮件系统组织
	 * */
	private boolean isCoreMail;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String[] getDomains() {
		return domains;
	}
	public void setDomains(String[] domains) {
		this.domains = domains;
	}
	public boolean isCas() {
		return isCas;
	}
	public void setCas(boolean isCas) {
		this.isCas = isCas;
	}
	public boolean isCoreMail() {
		return isCoreMail;
	}
	public void setCoreMail(boolean isCoreMail) {
		this.isCoreMail = isCoreMail;
	}
	
}
