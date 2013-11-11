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
package cn.vlabs.umt.services.user.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RandomUtil;

public class OauthClientBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3535772519561350223L;
	public static final String  STATUS_ACCEPT="accept";
	public static final String STATUS_APPLY="apply";
	public static final String STATUS_REFUSE="refuse";
	public static final String APP_TYPE_WEB_APP="webapp";
	public static final String APP_TYPE_PHONE_APP="phoneapp";
	
	private int id;
	private String clientId;
	private String clientSecret;
	private String scope;
	private String redirectURI;
	private String clientName;
	private String applicant;
	private String clientWebsite;
	private Date applicationTime;
	private String applicantPhone;
	private String contactInfo;
	private String description;
	private String status;
	private String thirdParty;
	
	//add by lvly @2013-7-5
	private int uid;
	private String company;
	private String appType;
	
	
	public String getAppTypeDesc(){
		if(APP_TYPE_PHONE_APP.equals(this.getAppType())){
			return "移动应用";
		}else if(APP_TYPE_WEB_APP.equals(this.getAppType())){
			return "网站接入";
		}
		return "";
	}
	
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getClientWebsite() {
		return clientWebsite;
	}
	public void setClientWebsite(String clientWebsite) {
		this.clientWebsite = clientWebsite;
	}
	public String getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(String thirdParty) {
		this.thirdParty = thirdParty;
	}
	
	public Map<String,String> getThirdPartyMap(){
		Map<String,String> result = new HashMap<String,String>();
		if(thirdParty!=null&&thirdParty.length()>0){
			String[] ss = thirdParty.split(";");
			for(String s:ss){
				result.put(s,s);
			}
		}
		return result;
	}
	
	public boolean isAccept(){
		return STATUS_ACCEPT.equals(status);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public Date getApplicationTime() {
		if(applicationTime==null){
			return new Date();
		}
		return applicationTime;
	}
	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
	}
	public String getApplicantPhone() {
		return applicantPhone;
	}
	public void setApplicantPhone(String applicantPhone) {
		this.applicantPhone = applicantPhone;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public String getStatusDisplay(){
		if(STATUS_ACCEPT.equals(status)){
			return "审核通过";
		}
		if(STATUS_APPLY.equals(status)){
			return "置为待审核";
		}
		if(STATUS_REFUSE.equals(status)){
			return "被拒绝";
		}
		return "";
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean validateScope(Set<String> scopes) {
		if(scopes==null||scopes.isEmpty()){
			return true;
		}else{
			return getScopeSet().containsAll(scopes);
		}
	}
	public Set<String> getScopeSet(){
		if(scope==null||scope.length()==0){
			return Collections.emptySet();
		}
		String[] v = scope.split(",");
		Set<String> all = new HashSet<String>();
		for(String s : v){
			all.add(s);
		}
		return all;
	}
	
}