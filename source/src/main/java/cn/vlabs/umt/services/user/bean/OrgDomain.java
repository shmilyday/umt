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

public class OrgDomain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 780116516536369814L;
	private int id;
	private String orgSymbol;
	private String orgName;
	private boolean isCas;
	private boolean isCoreMail;
	private int type;
	private String[] domain;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrgSymbol() {
		return orgSymbol;
	}
	public void setOrgSymbol(String orgSymbol) {
		this.orgSymbol = orgSymbol;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public boolean isCas() {
		return isCas;
	}
	public void setCas(boolean isCas) {
		this.isCas = isCas;
	}
	public boolean isCoreMail() {
		return isCoreMail;
	}
	public void setCoreMail(boolean isCoreMail) {
		this.isCoreMail = isCoreMail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String[] getDomain() {
		return domain;
	}
	public void setDomain(String[] domain) {
		this.domain = domain;
	}
	
	

}
