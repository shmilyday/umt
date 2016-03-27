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

public class CaApplication {
	
	private int id;
	private int uid;
	private String cn;
	private String dn;
	private int type;
	private Date valiFrom;
	private Date expirationOn;
	private String password;
	private int status;
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
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getValiFrom() {
		return valiFrom;
	}
	public void setValiFrom(Date valiFrom) {
		this.valiFrom = valiFrom;
	}
	public Date getExpirationOn() {
		return expirationOn;
	}
	public void setExpirationOn(Date expirationOn) {
		this.expirationOn = expirationOn;
	}
	public String getPassword() {
		return password;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	 
	public static final int STATUS_NORMAL=1;
	public static final int STATUS_EXPIRED=2;
	public static final int STATUS_DELETED=3;
	
	public static final int TYPE_EDUROMA=1;
	
	
	

}
