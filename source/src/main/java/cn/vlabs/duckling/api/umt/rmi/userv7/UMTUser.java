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
/*
c * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
package cn.vlabs.duckling.api.umt.rmi.userv7;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 用户信息
 * @author Fred Zhang (fred@cnic.cn)
 *
 */
@XStreamAlias("user")
public class UMTUser {
	public static final String FIELD_CSTNET_ID = "cstnet_id";
	public static final String FIELD_TRUE_NAME = "true_name";
	public UMTUser(){}
	/**
	 * 正常的创建方法
	 * @param username 无用，请勿复制
	 * @param truename 真是姓名
	 * @param email 主邮箱地址
	 * @param password 密码
	 * */
	public UMTUser(String trueName,String cstnetId){
		this.truename=trueName;
		this.cstnetId=cstnetId;
	}
	/**
	 * 从umt7.0开始 不再起作用，因为用户的umtId是umt生成，而不是指派
	 * @param umtId 无用，请勿复制
	 * @param truename 真是姓名
	 * @param cstnetId 主邮箱地址
	 * @param password 密码
	 * */
	public UMTUser(String umtId, String truename, String cstnetId){
		this.truename=truename;
		this.cstnetId=cstnetId;
		this.umtId=umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String getCstnetId() {
		if(cstnetId==null){
			return null;
		}
		return cstnetId.toLowerCase();
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
	 * 对应umt里面的umtId
	 * */
	private String umtId;
	/**
	 * 对应umt里面的cstnetId，为了兼容不得已名字未改，值发生改变
	 * */
	private String cstnetId;
	/**
	 * 密码
	 * */
	private String password;
	/**
	 * 真实姓名，未有改动
	 * */
	private String truename;
	
	/**
	 * 密保邮箱
	 * */
	private String securityEmail;
	
	/**
	 * 辅助邮箱邮箱，暂不开放设置辅助邮箱的api
	 **/
	private String[] secondaryEmails;
	
	/**
	 * 主账户激活状态
	 * */
	private String cstnetIdStatus;
	/**
	 *账户所属
	 **/
	private String authBy;
	
	
	
	public String getCstnetIdStatus() {
		return cstnetIdStatus;
	}
	public void setCstnetIdStatus(String cstnetIdStatus) {
		this.cstnetIdStatus = cstnetIdStatus;
	}
	public String getAuthBy() {
		return authBy;
	}
	public void setAuthBy(String authBy) {
		this.authBy = authBy;
	}
	public String[] getSecondaryEmails() {
		return secondaryEmails;
	}
	public String getSecurityEmail() {
		return securityEmail;
	}
	public void setSecurityEmail(String securityEmail) {
		this.securityEmail = securityEmail;
	}
	public void setSecondaryEmails(String[] secondaryEmails) {
		this.secondaryEmails = secondaryEmails==null?null:secondaryEmails.clone();
	}
	
}