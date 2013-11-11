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
package cn.vlabs.umt.services.session;

import java.util.Date;

/**
 * 这个类用来和用户会话界面上通信使用。
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 18, 2009 10:41:20 AM
 */
public class LoginRecordVO {
	public void setAppdesc(String appdesc) {
		this.appdesc = appdesc;
	}
	public String getAppdesc() {
		return appdesc;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppurl(String appurl) {
		this.appurl = appurl;
	}
	public String getAppurl() {
		return appurl;
	}
	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	public Date getLogintime() {
		return logintime;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getUserip() {
		return userip;
	}
	public void setId(int id){
		this.id=id;
	}
	public int getId(){
		return id;
	}
	private int id;
	private String appdesc;
	private String appname;
	private String appurl;
	private Date logintime;
	private String userip;
}