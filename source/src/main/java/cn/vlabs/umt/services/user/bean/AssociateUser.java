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

/**
 * 关联用户
 * @author lvly
 * @since 2013-3-12
 */
public class AssociateUser {
	private int id;
	private int uid;
	private int associateUid;
	private String[] appList;
	
	
	
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
	public int getAssociateUid() {
		return associateUid;
	}
	public void setAssociateUid(int associateUid) {
		this.associateUid = associateUid;
	}
	public String[] getAppList() {
		return appList;
	}
	public void setAppList(String[] appList) {
		this.appList = appList==null?null:appList.clone();
	}
	public static final String APP_DHOME="dhome";
	public static final String APP_DDL="ddl";
	public static final String APP_CORE_MAIL="coreMail";
	public static final String APP_CSP="csp";

}