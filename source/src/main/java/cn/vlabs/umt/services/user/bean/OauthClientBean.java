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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	/**
	 * 访问ddl的权限
	 * */
	public static final String DDL_SERVICE="ddlService";
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
	private String pwdType=PWD_TYPE_NONE;
	public static final String PWD_TYPE_NONE="none";
	public static final String PWD_TYPE_SHA="SHA";
	public static final String PWD_TYPE_MD5="MD5";
	public static final String PWD_TYPE_CRYPT="crypt";
	//tmp 2014-2-2
	private String userName;
	
	//add by lvly 140403,0为不需要,1为需要
	private int needOrgInfo;
	//add by lvly 140428
	private int logo100m100;
	
	private int logo64m64;
	
	private int logo32m32;
	
	private int logo16m16;
	
	private String logoCustom;
	
	private int defaultLogo;
	
	private String enableAppPwd="no";
	
	private boolean compulsionStrongPwd;
	
	public boolean isCompulsionStrongPwd() {
		return compulsionStrongPwd;
	}
	public void setCompulsionStrongPwd(boolean compulsionStrongPwd) {
		this.compulsionStrongPwd = compulsionStrongPwd;
	}
	public String getEnableAppPwd() {
		return enableAppPwd;
	}
	public void setEnableAppPwd(String enableAppPwd) {
		this.enableAppPwd = enableAppPwd;
	}
	public int getDefaultLogo() {
		return defaultLogo;
	}
	public void setDefaultLogo(int defaultLogo) {
		this.defaultLogo = defaultLogo;
	}
	public int getLogo100m100() {
		return logo100m100;
	}
	public void setLogo100m100(int logo100m100) {
		this.logo100m100 = logo100m100;
	}
	public int getLogo64m64() {
		return logo64m64;
	}
	public void setLogo64m64(int logo64m64) {
		this.logo64m64 = logo64m64;
	}
	public int getLogo32m32() {
		return logo32m32;
	}
	public void setLogo32m32(int logo32m32) {
		this.logo32m32 = logo32m32;
	}
	public int getLogo16m16() {
		return logo16m16;
	}
	public void setLogo16m16(int logo16m16) {
		this.logo16m16 = logo16m16;
	}
	public int getNeedOrgInfo() {
		return needOrgInfo;
	}
	public boolean isNeedOrgInfo(){
		return needOrgInfo==1;
	}

	public void setNeedOrgInfo(int needOrgInfo) {
		this.needOrgInfo = needOrgInfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAppTypeDesc(){
		if(APP_TYPE_PHONE_APP.equals(this.getAppType())){
			return "移动应用";
		}else if(APP_TYPE_WEB_APP.equals(this.getAppType())){
			return "网站接入";
		}
		return "";
	}
	
	public String getPwdType() {
		return pwdType;
	}

	public void setPwdType(String pwdType) {
		this.pwdType = pwdType;
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
	public boolean is64LogoDefault(){
		return this.defaultLogo==this.logo64m64;
	}
	public boolean is32LogoDefault(){
		return this.defaultLogo==this.logo32m32;
	}
	public boolean is16LogoDefault(){
		return this.defaultLogo==this.logo16m16;
	}
	public boolean is100LogoDefault(){
		return this.defaultLogo==this.logo100m100;
	}
	public String getLogoCustom() {
		return logoCustom;
	}
	public void setLogoCustom(String logoCustom) {
		this.logoCustom = logoCustom;
	}
	
	
}