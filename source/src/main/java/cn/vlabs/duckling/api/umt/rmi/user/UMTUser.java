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
package cn.vlabs.duckling.api.umt.rmi.user;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 用户信息 主要用于与umt通讯用的
 * @author Fred Zhang (fred@cnic.cn)
 * @author lvly@2013-3-22
 *
 */
@XStreamAlias("user")
public class UMTUser {
	public static final String FIELD_USER_NAME = "username";
	public static final String FIELD_TRUE_NAME = "truename";
	public static final String FIELD_EMAIL="email";
	public UMTUser(){}
	/**
	 * 严格意义来说，现在的email并不起作用，因为唯一标示是username
	 * @param username 用户账户名
	 * @param truename 用户真实姓名
	 * @param email 无用，废弃
	 * @param password 密码，现在收回应用修改密码的权限，无用，废弃
	 * */
	public UMTUser(String username, String truename, String email, String password){
		this.username=username;
		this.truename=truename;
		this.email=email;
		this.password=password;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getTruename() {
		return truename;
	}
	/**
	 * 用户账户名
	 * */
	private String username;
	/**
	 * 该值无意义，umt返回的email和username是一样的
	 * */
	private String email;
	/**
	 * 该值无意义，因为无法修改密码，也无法做什么
	 * */
	private String password;
	/**
	 * 用户姓名
	 * */
	private String truename;
}