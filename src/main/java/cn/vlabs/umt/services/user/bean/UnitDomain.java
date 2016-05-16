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


public class UnitDomain{
	private int id;
	private String symbol;
	private String name;
	private String rootDomain;
	private String mailDomain;
	private String enName;
	public static final String MAILDOMAIN_SPLIT=",";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRootDomain() {
		return rootDomain;
	}
	public void setRootDomain(String rootDomain) {
		this.rootDomain = rootDomain;
	}
	public String getMailDomain() {
		
		if(mailDomain==null||"".equals(mailDomain.trim())){
			return mailDomain;
		}
		if(!mailDomain.endsWith(MAILDOMAIN_SPLIT)){
			mailDomain+=MAILDOMAIN_SPLIT;
		}
		return mailDomain;
	}
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}

	
	
	

}
