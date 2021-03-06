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
package cn.vlabs.umt.services.user.bean;

import cn.vlabs.umt.services.user.Credential;


/**
 * 用户名密码方式的验证
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 11:34:20 AM
 */
public class UsernamePasswordCredential implements Credential {
	public UsernamePasswordCredential(String username, String password){
		this.username=username;
		this.password=password;
	}
	public String getPassword(){
		return password;
	}
	public String getUsername(){
		return username;
	}
	private String username;
	private String password;
}