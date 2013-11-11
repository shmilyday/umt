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
package cn.vlabs.umt.ui.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.User;

/** 
 * MyEclipse Struts
 * Creation date: 12-17-2009
 * 
 * XDoclet definition:
 * @struts.form name="updateUserForm"
 */
public class UpdateUserForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	private String password;
	private String truename;
	private String username;
	private String umtId;
	private String email;
	private String retype;

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return null;
	}
	
	public User getUser(){
		User u = new User();
		if (username!=null)
		{
			username=username.trim();
		}
		u.setCstnetId(username);
		u.setPassword(password);
		u.setTrueName(truename);
		u.setUmtId(umtId);
		return u;
	}
	
	public String getUmtId() {
		return umtId;
	}

	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
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
	 * Returns the truename.
	 * @return String
	 */
	public String getTruename() {
		return CommonUtils.trim(truename);
	}

	/** 
	 * Set the truename.
	 * @param truename The truename to set
	 */
	public void setTruename(String truename) {
		this.truename = truename;
	}

	/** 
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return CommonUtils.trim(username);
	}

	/** 
	 * Set the username.
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * Returns the email.
	 * @return String
	 */
	public String getEmail() {
		return CommonUtils.trim(email);
	}

	/** 
	 * Set the email.
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	 * Returns the retype.
	 * @return String
	 */
	public String getRetype() {
		return CommonUtils.trim(retype);
	}

	/** 
	 * Set the retype.
	 * @param retype The retype to set
	 */
	public void setRetype(String retype) {
		this.retype = retype;
	}
}