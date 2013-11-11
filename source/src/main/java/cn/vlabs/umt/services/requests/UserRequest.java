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
package cn.vlabs.umt.services.requests;

import java.util.Date;

public class UserRequest {
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getTruename() {
		return truename;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}
	public String getOrgnization() {
		return orgnization;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return email;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperator() {
		return operator;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	private int id;
	private String username;
	private String truename;
	private String password;
	private String phonenumber;
	private String orgnization;
	private String email;
	private Date createTime;
	private int state=INIT;
	private String operator;
	
	public static final int INIT=1;		//初始状态
	public static final int	APPROVE=2;		//同意该请求
	public static final int	DENY=3;			//拒绝该请求
	public static final int	SILENCE_DENY=4;	//直接删除请求
	public static final int ALL=5;				//所有的请求
}