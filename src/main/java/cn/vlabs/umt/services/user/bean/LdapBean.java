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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.vlabs.umt.common.EmailUtil;

import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class LdapBean {
	private static final Logger LOG=Logger.getLogger(LdapBean.class);
	private int id;
	private String rdn;
	private int uid;
	/**
	 * LDAP应用的类型：普通的LDAP应用、WIFI应用
	 */
	private String type;
	private String clientName;
	private String description;
	//申请者名称
	private String applicant;
	//申请人所在单位：
	private String company;
	//申请人联系电话：
	private String contactInfo;
	//发布范围：
	private String pubScope;
	
	private String appStatus;
	private Date createTime;
	private String userName;
	private String userCstnetId;
	
	private String priv;
	
	private String ldapPassword;
	public static final String PRIV_OPEN="open";
	public static final String PRIV_NEED_APPLY="needApply";
	public static final String PRIV_CLOSED="closed";
	public static final String APP_STATUS_ACCEPT="accept";
	public static final String APP_STATUS_REFUSE="refuse";
	public static final String APP_STATUS_APPLY="apply";
	
	public String getAppStatus() {
		return appStatus;
	}
	public String getAppStatusDisplay(){
		switch(appStatus){
		case "accept":{
			return "审核通过";
		}
		case "refuse":{
			return "拒绝";
		}
		case "apply":{
			return "申请中";
		}
		default:{
			return "未知";
		}
		
		}
	}
	
	public String toJson(){
		JSONObject jsonObject=new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("rdn", rdn);
			jsonObject.put("uid", uid);
			jsonObject.put("clientName", clientName);
			jsonObject.put("description", description);
			jsonObject.put("applicant", applicant);
			jsonObject.put("company", company);
			jsonObject.put("contactInfo", contactInfo);
			jsonObject.put("appStatus",appStatus);
			jsonObject.put("priv", this.priv);
			jsonObject.put("ldapPassword", ldapPassword);
			jsonObject.put("pubScope", pubScope);
			return jsonObject.toString();
		} catch (JSONException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
		
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	public LdapBean(){}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRdn() {
		return rdn;
	}
	public void setRdn(String rdn) {
		this.rdn = rdn;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	public int getUid() {
		return uid;
	}
	public String getPriv() {
		return priv;
	}
	public String getPrivDisplay() {
		switch(priv){
			case "needApply":{
				return "公开需审核";
			}
			case "open":{
				return "公开";
			}
			case "closed":{
				return "关闭";
			}
			default:{
				return "未知";
			}
		}
	}
	public void setPriv(String priv) {
		this.priv = priv;
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
	public boolean isLdapClose(){
		return "no".equals(this.priv)||!APP_STATUS_ACCEPT.equals(this.appStatus);
	}
	public boolean isLdapOpen(){
		return "yes".equals(this.priv)&&APP_STATUS_ACCEPT.equals(this.appStatus);
	}
	public String getUserCstnetId() {
		return userCstnetId;
	}
	public void setUserCstnetId(String userCstnetId) {
		this.userCstnetId = userCstnetId;
	}
	public String getLdapPassword() {
		return ldapPassword;
	}
	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}
	public String getPubScope() {
		return pubScope;
	}
	public void setPubScope(String pubScope) {
		this.pubScope = pubScope;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean inScope(String address){
		if (StringUtils.isEmpty(pubScope)){
			return true;
		}else{
			String domain = EmailUtil.extractDomain(address);
			return (pubScope.equals(domain));
		}
	}
	public boolean isWifiApp(){
		return "wifi".equals(type);
	}
}
