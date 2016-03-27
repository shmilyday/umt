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

/**
 * 用户登录名信息
 * @author lvly
 * @since 2013-3-5
 */
public class LoginNameInfo implements Serializable{
	private static final long serialVersionUID = -8783382309450733615L;
	private int id;
	private int uid;
	private String loginName;
	private String type;
	private String tmpLoginName;
	private String status;
	public LoginNameInfo(){
		
	}
	public LoginNameInfo(int uid,String type,String loginName,String status){
		this.uid=uid;
		this.type=type;
		this.loginName=loginName;
		this.status=status;
	}
	
	public static final String STATUS_ACTIVE="active";
	public static final String STATUS_TEMP="temp";
	public String getStatusDisplay(){
		if(STATUS_ACTIVE.equals(this.status)){
			return "已激活";
		}else if(STATUS_TEMP.equals(this.status)){
			return "未激活";
		}
		return "";
	}
	
	public static final String LOGINNAME_TYPE_PRIMARY="primary";
	public static final String LOGINNAME_TYPE_SECONDARY="secondary";
	public static final String LOGINNAME_TYPE_MOBILE="mobile";
	public static final String LOGINNAME_TYPE_LDAP="ldap";

	public boolean isTmpAndSecondary(){
		return this.type.equals(LOGINNAME_TYPE_SECONDARY)&&this.status.equals(STATUS_TEMP);
	}

	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTmpLoginName() {
		return tmpLoginName;
	}
	public void setTmpLoginName(String tmpLoginName) {
		this.tmpLoginName = tmpLoginName;
	}

}