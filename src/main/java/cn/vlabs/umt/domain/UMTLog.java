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
package cn.vlabs.umt.domain;

import java.util.Date;

import net.duckling.cloudy.common.CommonUtils;

/**
 * umt日志信息
 * 
 * @author lvly
 * @since 2013-1-23
 */
public class UMTLog {
	public UMTLog(){
		this.appName=DEFAULT_APP_NAME;
	}
	/**
	 * 触发行为，详见 常量 EVENT_TYPE_XXX;
	 * */
	private String eventType;
	public static final String EVENT_TYPE_LOG_IN="login";
	public static final String EVENT_TYPE_LOG_OUT="logout";
	public static final String EVENT_TYPE_CHANGE_SECURITY_EMAIL="change_security_email";
	public static final String EVENT_TYPE_BIND_PHONE="log_in";
	public static final String EVENT_TYPE_CHANGE_PASSWORD="change_password";
	public static final String EVENT_TYPE_CHANGE_LOGIN_NAME="change_login_name";
	/**
	 * 应用名称
	 * */
	private String appName;
	private static final String DEFAULT_APP_NAME="umt";
	/**
	 * 应用地址
	 * */
	private String appUrl;
	/**
	 * 用户名
	 * */
	private int uid;
	/**
	 * 用户IP
	 * */
	private String userIp;
	/**
	 * 发生时间
	 * */
	private Date occurTime; 
	/**
	 * 浏览器信息
	 * */
	private String browserType; 
	/**
	 *  未知
	 * */
	private String remark;
	
	private String country;
	private String city;
	private String province;
	private int id;
	private boolean sendWarnEmail;
	private boolean isCstnetUnit;
	private String unitName;
	private boolean fromDip;
	
	public boolean isFromDip() {
		return fromDip;
	}
	public void setFromDip(boolean fromDip) {
		this.fromDip = fromDip;
	}
	public boolean isCstnetUnit() {
		return isCstnetUnit;
	}
	public void setCstnetUnit(boolean isCstnetUnit) {
		this.isCstnetUnit = isCstnetUnit;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public boolean isSendWarnEmail() {
		return sendWarnEmail;
	}
	public void setSendWarnEmail(boolean sendWarnEmail) {
		this.sendWarnEmail = sendWarnEmail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountry() {
		return CommonUtils.killNull(country);
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return CommonUtils.killNull(city);
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return CommonUtils.killNull(province);
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int userName) {
		this.uid = userName;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public GEOInfo getGEOInfo(){
		GEOInfo info=new GEOInfo();
		info.setCity(this.city);
		info.setCountry(this.country);
		info.setCstnetUnit(this.isCstnetUnit);
		info.setFromDip(this.fromDip);
		info.setIp(this.userIp);
		info.setProvince(this.province);
		info.setUnitName(this.unitName);
		return info;
	}
	public Date getOccurTime() {
		return occurTime;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public boolean isNullGEO(){
		return CommonUtils.isNull(this.city)&&CommonUtils.isNull(this.province)&&CommonUtils.isNull(this.country);
	}
	private String displayWhere(){
		if("中国".equals(getCountry())){
			if("北京".equals(getProvince())){
				return getProvince()+"市";
			}else if("上海".equals(getProvince())){
				return getProvince()+"市";
			}else if("天津".equals(getProvince())){
				return getProvince()+"市";
			}else if("重庆".equals(getProvince())){
				return getProvince()+"市";
			}else{
				if(CommonUtils.isNull(getProvince())&&CommonUtils.isNull(getCity())){
					return "未知地区";
				}else{
					return getProvince()+"省 "+getCity()+"市";
				}
			}
		}
		else{
			if(CommonUtils.isNull(getCountry())&&CommonUtils.isNull(getProvince())&&CommonUtils.isNull(getCity())){
				return "未知地区";
			}else{
				return getCountry()+" "+getProvince()+" "+getCity();
			}
		}
	}
	
	
	public String displayGEO(){
		if(isCstnetUnit){
			String where=displayWhere();
			if(!CommonUtils.isNull(where)){
				where="<span>("+where+")</span>";
			}
			return CommonUtils.isNull(unitName)?"科技网"+where:unitName+where;
		}else{
			return displayWhere();
		}
	}
}