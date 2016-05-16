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
package cn.vlabs.umt.services.session.impl;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.time.DateUtils;

public class LoginRecord {
	public void setGSessionid(String gSessionid) {
		this.gSessionid = gSessionid;
	}
	public String getGSessionid() {
		return gSessionid;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppname() {
		return appname;
	}
	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}
	public Date getLastupdate() {
		return lastupdate;
	}
	public void setAppSessionid(String appSessionid) {
		this.appSessionid = appSessionid;
	}
	public String getAppSessionid() {
		return appSessionid;
	}
	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}
	public String getLogoutURL() {
		return logoutURL;
	}
	public void setAppType(String appType) {
		this.appType=appType;
	}
	public String getAppType(){
		return appType;
	}
	
	public void logout(Date deadline) {
		Date timeout=DateUtils.addMinutes(lastupdate, 30);
		if (timeout.before(deadline)){
			String sessionkey = sessionkeys.get(appType);
			if (sessionkey!=null){
				GetMethod method = new GetMethod(logoutURL);
				method.setRequestHeader("Connection", "close");  
				method.setRequestHeader("Cookie", sessionkey+"="+appSessionid);
				HttpClient client = new HttpClient();
				try {
					client.executeMethod(method);
				} catch (Exception e) {
					
				}finally{
					client.getHttpConnectionManager().closeIdleConnections(0);
				}
			}
		}
	}
	
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getUserip() {
		return userip;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	public Date getLogintime() {
		return logintime;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public int getId(){
		return this.id;
	}
	private static HashMap<String, String>sessionkeys;
	static{
		sessionkeys = new HashMap<String, String>();
		sessionkeys.put("JSP", "JSESSIONID");
		sessionkeys.put("ASP", "SessionID");
		sessionkeys.put("PHP", "PHPSESSID");
	}
	
	private String gSessionid;
	private String appname;
	private Date lastupdate;
	private String appSessionid;
	private String logoutURL;
	private String userip;
	private Date logintime;
	private String appType;
	private int id;

}