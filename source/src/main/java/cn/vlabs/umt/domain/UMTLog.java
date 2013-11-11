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
package cn.vlabs.umt.domain;

import java.util.Date;

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
	
	
	
	
}