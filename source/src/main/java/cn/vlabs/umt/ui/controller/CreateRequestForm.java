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
package cn.vlabs.umt.ui.controller;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.User;

public class CreateRequestForm {
	/** password property */
	private String password;

	/** username property */
	private String username;

	/** validcode property */
	private String validcode;
	private String tempSecurityEmail;
	
	private String orgnization;
	private String phonenumber;
	private String truename;
	private String mobilePhone;
	
	/*
	 * Generated Methods
	 */

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}


	public String getTempSecurityEmail() {
		return tempSecurityEmail;
	}

	public void setTempSecurityEmail(String securityEmail) {
		this.tempSecurityEmail = securityEmail;
	}


	/** 
	 * Returns the password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * Set the password.
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return CommonUtils.trim(username);
	}

	private String trim(String value){
		if (value!=null)
		{
			return value.trim();
		}
		return value;
	}
	/** 
	 * Set the username.
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * Returns the validcode.
	 * @return String
	 */
	public String getValidcode() {
		return CommonUtils.trim(validcode);
	}

	/** 
	 * Set the validcode.
	 * @param validcode The validcode to set
	 */
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}

	public void setPhonenumber(String phonenumer) {
		this.phonenumber = phonenumer;
	}

	public String getPhonenumber() {
		return CommonUtils.trim(phonenumber);
	}

	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}

	public String getOrgnization() {
		return CommonUtils.trim(orgnization);
	}
	public User getUser(){
		User user = new User();
		user.setCstnetId(trim(getUsername()));
		user.setPassword(trim(password));
		user.setTrueName(trim(getTruename()));
		user.setSecurityEmail(trim(getTempSecurityEmail()));
		return user;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}

	public String getTruename() {
		return truename;
	}
}