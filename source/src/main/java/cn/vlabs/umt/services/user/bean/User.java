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
import java.util.Date;

import cn.vlabs.commons.principal.UserPrincipal;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4119002169055153608L;
	/**
	 * 用户id，umt内部使用
	 * */
	private int id;
	/**
	 * 对外的用户标识，老数据是邮箱，新数据是随机生成的字符串
	 * */
	private String umtId;
	/**
	 * 密码，经过加密
	 * */
	private String password;
	/**
	 * 用户昵称
	 * */
	private String trueName;
	/**
	 * 用户所属，主要是指当前登录名的授权
	 * */
	private String type;
	/**
	 * 密保邮箱，激活成功则在这里记一下，如果没有激活或者未设置，则为空
	 * */
	private String securityEmail;
	private Date createTime;
	/**
	 * 主邮箱地址
	 * */
	private String cstnetId;
	/**
	 * 辅助邮箱地址
	 * */
	private String[] secondaryEmails;
	/**
	 * 发送开关
	 * */
	private boolean sendGEOEmailSwitch;
	/**
	 * 用户来源是UMT
	 * */
	public static final String USER_TYPE_UMT="umt";
	/**
	 * 用户来源是CORE_MAIL
	 * */
	public static final String USER_TYPE_CORE_MAIL="coreMail";
	/**
	 * 用户来源是mail和umt共有
	 * */
	public static final String USER_TYPE_MAIL_AND_UMT="uc";
	
	/**
	 * 用户锁定与否状态
	 * */
	private String accountStatus;
	
	public static final String ACCOUNT_STATUS_NORMAL="normal";
	public static final String ACCOUNT_STATUS_LOCKED="locked";
	public static final String ACCOUNT_STATUS_LIMIT="limit";
	
	
	public boolean isSendGEOEmailSwitch() {
		return sendGEOEmailSwitch;
	}
	public void setSendGEOEmailSwitch(boolean sendGEOEmailSwitch) {
		this.sendGEOEmailSwitch = sendGEOEmailSwitch;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	/**
	 * @return the authBy
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 默认构造函数，authBy是 umt
	 * */
	public User(){
		this.type=USER_TYPE_UMT;
	}
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	public String getSecurityEmail() {
		return securityEmail;
	}
	public void setSecurityEmail(String securityEmail) {
		this.securityEmail = securityEmail;
	}
	public String getCstnetId() {
		if(cstnetId==null){
			return null;
		}
		return cstnetId.toLowerCase();
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String[] getSecondaryEmails() {
		return secondaryEmails;
	}
	public void setSecondaryEmails(String[] secondaryEmail) {
		if(secondaryEmail==null){
			this.secondaryEmails=null;
		}else{
			this.secondaryEmails=secondaryEmail.clone();
		}
	}
	public static boolean isBuildinAuthBy(String type){
		return USER_TYPE_CORE_MAIL.equalsIgnoreCase(type)||USER_TYPE_MAIL_AND_UMT.equalsIgnoreCase(type)||USER_TYPE_UMT.equalsIgnoreCase(type);
	}
	public boolean isCoreMailOrUc(){
		return USER_TYPE_CORE_MAIL.equals(this.getType())||USER_TYPE_MAIL_AND_UMT.equals(this.getType());
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 为了兼容老接口，最好不要用这个方法，里面的值都在user提取就好
	 * @return
	 */
	public UserPrincipal getUserPrincipal() {
		return new UserPrincipal(getCstnetId(),getTrueName(), getCstnetId(),"");
	}
}