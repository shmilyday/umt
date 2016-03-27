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

import cn.vlabs.umt.services.user.Credential;


public class ThirdPartyCredential implements Credential,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3672450617342275116L;
	private String username;
	private String password;
	private String authBy;
	public ThirdPartyCredential(String username, String password,String authBy){
		this.username=username;
		this.password=password;
		this.authBy = authBy;
	}
	public String getPassword(){
		return password;
	}
	public String getUsername(){
		return username;
	}
	public String getAuthBy()
	{
		return authBy;
	}


}